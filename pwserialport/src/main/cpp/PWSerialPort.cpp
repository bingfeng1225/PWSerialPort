//
// Created by Nick on 2019-09-29.
//

#include "PWSerialPort.h"

PWSerialPort::PWSerialPort() {
    this->fd = -1;
    this->parity = 0;
    this->baudrate = 0;
    this->dataBits = 8;
    this->stopBits = 1;
    this->flowControl = 0;
    this->filePath = "";
}

PWSerialPort::~PWSerialPort() {

}

int PWSerialPort::getFd() const {
    return this->fd;
}

void PWSerialPort::setFd(int fd) {
    this->fd = fd;
}

int PWSerialPort::getParity() const {
    return this->parity;
}

void PWSerialPort::setParity(int parity) {
    this->parity = parity;
}

int PWSerialPort::getBaudrate() const {
    return this->baudrate;
}

void PWSerialPort::setBaudrate(int baudrate) {
    this->baudrate = baudrate;
}

int PWSerialPort::getDataBits() const {
    return this->dataBits;
}

void PWSerialPort::setDataBits(int dataBits) {
    this->dataBits = dataBits;
}

int PWSerialPort::getStopBits() const {
    return this->stopBits;
}

void PWSerialPort::setStopBits(int stopBits) {
    this->stopBits = stopBits;
}

int PWSerialPort::getFlowControl() const {
    return this->flowControl;
}

void PWSerialPort::setFlowControl(int flowControl) {
    this->flowControl = flowControl;
}

const std::string &PWSerialPort::getFilePath() const {
    return filePath;
}

void PWSerialPort::setFilePath(const std::string &filePath) {
    PWSerialPort::filePath = filePath;
}

int PWSerialPort::serialPortOpen() {
    if (this->filePath.empty()) {
        LOGE("Serial port address can not empty");
        return -1;
    }
    /*打开串口*/
    this->fd = open(this->filePath.data(), O_RDWR | O_NOCTTY | O_NDELAY);
    if (this->fd == -1) {
        LOGE("Open serial port file failed!");
        return -1;
    }
    /*清除串口非阻塞标志*/
    if (fcntl(this->fd, F_SETFL, 0) < 0) {
        LOGE("fcntl failed!");
        return -1;
    }
    return this->serialPortConfig();
}

speed_t PWSerialPort::findBaudrate() {
    switch (this->baudrate) {
        case 0:
            return B0;
        case 50:
            return B50;
        case 75:
            return B75;
        case 110:
            return B110;
        case 134:
            return B134;
        case 150:
            return B150;
        case 200:
            return B200;
        case 300:
            return B300;
        case 600:
            return B600;
        case 1200:
            return B1200;
        case 1800:
            return B1800;
        case 2400:
            return B2400;
        case 4800:
            return B4800;
        case 9600:
            return B9600;
        case 19200:
            return B19200;
        case 38400:
            return B38400;
        case 57600:
            return B57600;
        case 115200:
            return B115200;
        case 230400:
            return B230400;
        case 460800:
            return B460800;
        case 500000:
            return B500000;
        case 576000:
            return B576000;
        case 921600:
            return B921600;
        case 1000000:
            return B1000000;
        case 1152000:
            return B1152000;
        case 1500000:
            return B1500000;
        case 2000000:
            return B2000000;
        case 2500000:
            return B2500000;
        case 3000000:
            return B3000000;
        case 3500000:
            return B3500000;
        case 4000000:
            return B4000000;
        default:
            return -1;
    }
}

int PWSerialPort::serialPortConfig() {
    struct termios options;
    /*获取终端属性*/
    if (tcgetattr(this->fd, &options) < 0) {
        LOGE("tcgetattr error");
        return -1;
    }
    /*设置输入输出波特率，两者保持一致*/
    speed_t speed = this->findBaudrate();
    if (speed == -1) {
        LOGE("Unkown baudrate!");
        return -1;
    }
    cfmakeraw(&options);
    cfsetispeed(&options, speed);
    cfsetospeed(&options, speed);

    /*设置数据位*/
    options.c_cflag &= ~CSIZE;//屏蔽其它标志位
    switch (this->dataBits) {
        case 5:
            options.c_cflag |= CS5;
            break;
        case 6:
            options.c_cflag |= CS6;
            break;
        case 7:
            options.c_cflag |= CS7;
            break;
        case 8:
            options.c_cflag |= CS8;
            break;
        default:
            LOGE("Unkown data bits!");
            return -1;
    }

    /*设置校验位*/
    switch (parity) {
        case 0:
            options.c_cflag &= ~PARENB;   //无奇偶校验位
            break;
        case 1:
            options.c_cflag |= PARENB;   //有奇偶校验位
            options.c_cflag |= PARODD;   //奇校验
            options.c_iflag |= INPCK;    //使奇偶校验起生效效
            break;
        case 2:
            options.c_cflag |= PARENB;   //有奇偶校验位
            options.c_cflag &= ~PARODD;  //奇校验
            options.c_iflag |= INPCK;    //使奇偶校验起生效效
            break;
        default:
            LOGE("Unkown parity!");
            return -1;
    }

    /*设置停止位*/
    switch (this->stopBits) {
        case 1:
            options.c_cflag &= ~CSTOPB;//CSTOPB：使用一位停止位
            break;
        case 2:
            options.c_cflag |= CSTOPB;//CSTOPB：使用两位停止位
            break;
        default:
            LOGE("Unkown stop bits!");
            return -1;
    }

    /*设置数据流控制*/
    switch (this->flowControl) {
        case 0://不进行流控制
            options.c_cflag &= ~CRTSCTS;
            break;
        case 1://进行硬件流控制
            options.c_cflag |= CRTSCTS;
            break;
        case 2://进行软件流控制
            options.c_cflag |= IXON | IXOFF | IXANY;
            break;
        default:
            LOGE("Unkown flow control!");
            return -1;
    }
    /*设置控制模式*/
    options.c_cflag |= CLOCAL;//保证程序不占用串口
    options.c_cflag |= CREAD;//保证程序可以从串口中读取数据

    /*设置输出模式为原始输出*/
    options.c_oflag &= ~OPOST;//OPOST：若设置则按定义的输出处理，否则所有c_oflag失效
    /*设置本地模式为原始模式*/
    options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
    /*
     *ICANON：允许规范模式进行输入处理
     *ECHO：允许输入字符的本地回显
     *ECHOE：在接收EPASE时执行Backspace,Space,Backspace组合
     *ISIG：允许信号
     */
    /*设置等待时间和最小接受字符*/
    options.c_cc[VTIME] = 0;//可以在select中设置
    options.c_cc[VMIN] = 1;//最少读取一个字符
    /*如果发生数据溢出，只接受数据，但是不进行读操作*/
    tcflush(this->fd, TCIFLUSH);
    /*激活配置*/
    if (tcsetattr(this->fd, TCSANOW, &options) < 0) {
        LOGE("tcsetattr failed");
        return -1;
    }
    return 0;
}

int PWSerialPort::serialPortSelect() {
    /*将文件描述符加入读描述符集合*/
    if (this->fd == -1) {
        return -1;
    }
    fd_set rfds;

    FD_ZERO(&rfds);
    FD_SET(this->fd, &rfds);

    struct timeval time;
    /*设置超时*/
    time.tv_sec = 1;
    time.tv_usec = 0;
    /*实现串口的多路I/O*/
    return select(this->fd + 1, &rfds, NULL, NULL, &time);
}

int PWSerialPort::serialPortRead(BYTE *buffer, size_t len) {
    int ret = this->serialPortSelect();
    if (ret == 0) { //超时
        return ret;
    } else if (ret == -1) { //失败
        return ret;
    }
    return static_cast<int>(read(this->fd, buffer, len));
}

int PWSerialPort::serialPortWrite(BYTE *buffer, size_t len) {
    if (this->fd == -1) {
        return -1;
    }
    size_t ret = write(this->fd, buffer, len);
    if (ret == -1) {
        return -1;
    }
    fdatasync(this->fd);
    fsync(this->fd);
    return static_cast<int>(ret);
}

void PWSerialPort::serialPortClose() {
    if (this->fd != -1) {
        close(this->fd);
        this->fd = -1;
    }
}





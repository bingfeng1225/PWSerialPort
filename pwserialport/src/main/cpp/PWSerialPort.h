//
// Created by Nick on 2019-09-29.
//

#ifndef SERIALPORT_PWSERIALPORT_H
#define SERIALPORT_PWSERIALPORT_H

#include <string>
#include <vector>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <unistd.h>
#include <termios.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/errno.h>
#include "android/log.h"

typedef unsigned char BYTE;
static const char *TAG = "PWSerialPort";

class PWSerialPort {
public:
    PWSerialPort();

    ~PWSerialPort();

    int getFd() const;

    void setFd(int fd);

    int getParity() const;

    void setParity(int parity);

    int getBaudrate() const;

    void setBaudrate(int baudrate);

    int getDataBits() const;

    void setDataBits(int dataBits);

    int getStopBits() const;

    void setStopBits(int stopBits);

    int getFlowControl() const;

    void setFlowControl(int flowControl);

    const std::string &getFilePath() const;

    void setFilePath(const std::string &filePath);

    int serialPortOpen();

    int serialPortConfig();

    speed_t findBaudrate();

    int serialPortSelect();

    void serialPortClose();
private:
    int fd;
    int parity;
    int baudrate;
    int dataBits;
    int stopBits;
    int flowControl;
    std::string filePath;
};

#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#endif //SERIALPORT_PWSERIALPORT_H

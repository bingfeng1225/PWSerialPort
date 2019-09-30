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


typedef unsigned char BYTE;

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

    void serialPortClose();

    int serialPortRead(BYTE *buffer, size_t len);

    int serialPortWrite(BYTE *buffer, size_t len);
private:
    int serialPortConfig();

    speed_t findBaudrate();

private:
    int fd;
    int parity;
    int baudrate;
    int dataBits;
    int stopBits;
    int flowControl;
    std::string filePath;
};


#endif //SERIALPORT_PWSERIALPORT_H

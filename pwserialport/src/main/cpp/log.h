//
// Created by Nick on 2019-09-29.
//

#ifndef SERIALPORT_LOG_H
#define SERIALPORT_LOG_H

#include "android/log.h"

static const char *TAG = "SerialPort";

#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#endif //SERIALPORT_LOG_H

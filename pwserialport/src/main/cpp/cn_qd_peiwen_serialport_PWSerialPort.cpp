/*
 * Copyright 2009-2011 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include "PWSerialPort.h"
#include "cn_qd_peiwen_serialport_PWSerialPort.h"

/*
 * Class:     PWSerialPort
 * Method:    open
 */
JNIEXPORT jlong JNICALL
Java_cn_qd_peiwen_serialport_PWSerialPort_open
        (JNIEnv *env, jobject thiz, jstring path, jint baudrate, jint stopBits, jint dataBits,
         jint parity, jint flowControl) {
    PWSerialPort *serial = new PWSerialPort();
    serial->setParity(parity);
    serial->setBaudrate(baudrate);
    serial->setDataBits(dataBits);
    serial->setStopBits(stopBits);
    serial->setFlowControl(flowControl);
    const char *path_utf = env->GetStringUTFChars(path, 0);
    serial->setFilePath(path_utf);
    env->ReleaseStringUTFChars(path, path_utf);
    if (serial->serialPortOpen() == -1) {
        delete serial;
        return -1;
    }
    return (jlong) serial;
}

/*
 * Class:     PWSerialPort
 * Method:    select
 */
JNIEXPORT jint JNICALL
Java_cn_qd_peiwen_serialport_PWSerialPort_select
        (JNIEnv *env, jobject thiz, jlong serialPort) {
    PWSerialPort *serial = (PWSerialPort *) serialPort;
    return serial->serialPortSelect();
}

/*
 * Class:     PWSerialPort
 * Method:    descriptor
 */
JNIEXPORT jobject JNICALL
Java_cn_qd_peiwen_serialport_PWSerialPort_descriptor
        (JNIEnv *env, jobject thiz, jlong serialPort) {
    PWSerialPort *serial = (PWSerialPort *) serialPort;
    jclass cFileDescriptor = env->FindClass("java/io/FileDescriptor");
    jmethodID iFileDescriptor = env->GetMethodID(cFileDescriptor, "<init>", "()V");
    jfieldID descriptorID = env->GetFieldID(cFileDescriptor, "descriptor", "I");
    jobject fileDescriptor = env->NewObject(cFileDescriptor, iFileDescriptor);
    env->SetIntField(fileDescriptor, descriptorID, (jint) serial->getFd());
    return fileDescriptor;
}



/*
 * Class:     PWSerialPort
 * Method:    close
 */
JNIEXPORT void JNICALL
Java_cn_qd_peiwen_serialport_PWSerialPort_close
        (JNIEnv *env, jobject thiz, jlong serialPort) {
    PWSerialPort *serial = (PWSerialPort *) serialPort;
    serial->serialPortClose();
    delete serial;
}


/*
 * Class:     PWSerialPort
 * Method:    writeFile
 */
JNIEXPORT void JNICALL
Java_cn_qd_peiwen_serialport_PWSerialPort_writeFile
        (JNIEnv *env, jclass cls, jstring path, jstring content) {
    jboolean iscopy;
    const char *path_utf = env->GetStringUTFChars(path, &iscopy);
    int fd = open(path_utf, O_CREAT | O_RDWR,0777);
    if (fd == -1) {
        LOGE("Cannot open file (%s) %d", path_utf, errno);
        return;
    }
    const char *content_utf = env->GetStringUTFChars(content, &iscopy);
    int len = write(fd, content_utf, strlen(content_utf));
    env->ReleaseStringUTFChars(content, content_utf);
    if (len == -1) {
        LOGE("Cannot write file (%s) %d", path_utf, errno);
    }
    env->ReleaseStringUTFChars(path, path_utf);
    fsync(fd);
    close(fd);
}
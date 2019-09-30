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
#include "log.h"
#include "SerialPort.h"
#include "PWSerialPort.h"

/*
 * Class:     SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jlong JNICALL
Java_cn_qd_peiwen_serialport_SerialPort_open
        (JNIEnv *env, jobject thiz, jstring path, jint baudrate, jint stopBits, jint dataBits,
         jint parity, jint flowControl) {
    PWSerialPort *serial = new PWSerialPort();
    serial->setParity(parity);
    serial->setBaudrate(baudrate);
    serial->setDataBits(dataBits);
    serial->setStopBits(stopBits);
    serial->setFlowControl(flowControl);
    jboolean iscopy;
    const char *path_utf = env->GetStringUTFChars(path, &iscopy);
    serial->setFilePath(path_utf);
    env->ReleaseStringUTFChars(path, path_utf);
    if (serial->serialPortOpen() == -1) {
        delete serial;
        return NULL;
    }
    return (jlong) serial;
}

/*
 * Class:     SerialPort
 * Method:    read
 */
JNIEXPORT jint JNICALL
Java_cn_qd_peiwen_serialport_SerialPort_read
        (JNIEnv *env, jobject thiz, jlong addr, jbyteArray buffer, jint len) {
    BYTE data[len];
    PWSerialPort *serial = (PWSerialPort *) addr;
    int ret = serial->serialPortRead(data, len);
    if (ret > 0) {
        jbyte *bytes = reinterpret_cast<jbyte *>(data);
        env->SetByteArrayRegion(buffer, 0, ret, bytes);
    }
    return ret;
}
/*
 * Class:     SerialPort
 * Method:    write
 */
JNIEXPORT jint JNICALL
Java_cn_qd_peiwen_serialport_SerialPort_write
        (JNIEnv *env, jobject thiz, jlong addr, jbyteArray buffer, jint len) {

    jbyte *data = env->GetByteArrayElements(buffer, 0);

    BYTE *bytes = reinterpret_cast<BYTE *>(data);

    PWSerialPort *serial = (PWSerialPort *) addr;

    int ret = serial->serialPortWrite(bytes, len);

    env->ReleaseByteArrayElements(buffer, data, 0);

    return ret;
}

/*
 * Class:     SerialPort
 * Method:    close
 */
JNIEXPORT void JNICALL
Java_cn_qd_peiwen_serialport_SerialPort_close
        (JNIEnv *env, jobject thiz, jlong addr) {
    PWSerialPort *serial = (PWSerialPort *) addr;
    serial->serialPortClose();
    delete serial;
}


/*
 * Class:     SerialPort
 * Method:    writeFile
 */
JNIEXPORT void JNICALL
Java_cn_qd_peiwen_serialport_SerialPort_writeFile
        (JNIEnv *env, jclass cls, jstring path, jstring content) {
    jboolean iscopy;
    const char *path_utf = env->GetStringUTFChars(path, &iscopy);
    int fd = open(path_utf, O_CREAT | O_RDWR);
    env->ReleaseStringUTFChars(path, path_utf);
    if (fd == -1) {
        LOGE("Cannot open file %d", errno);
        return;
    }
    const char *content_utf = env->GetStringUTFChars(content, &iscopy);
    int len = write(fd, content_utf, strlen(content_utf));
    env->ReleaseStringUTFChars(content, content_utf);
    if (len == -1) {
        LOGE("Cannot write file %d", errno);
    }
    close(fd);
}


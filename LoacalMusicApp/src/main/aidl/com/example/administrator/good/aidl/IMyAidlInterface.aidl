// IMyAidlInterface.aidl
package com.example.administrator.good.aidl;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
        void startPlay(int positison);
        void pausePlay(int positison);
        void resumPlay(int positison);
        void stopPlay(int positison);
        void up(int position);
        void next(int position);
        void setCurrentProcess(int cupc);
        void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}

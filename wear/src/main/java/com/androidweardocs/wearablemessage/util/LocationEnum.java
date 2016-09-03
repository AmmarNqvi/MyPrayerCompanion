
package com.androidweardocs.wearablemessage.util;

import android.content.Context;
import android.location.Location;

import com.androidweardocs.wearablemessage.R;


public enum LocationEnum {
    first(1, 34.08, 49.7), check(2, 38.25, 48.28), trd(3,
            37.53, 45d), frt(4, 32.65, 51.67), fft(5, 31.52,
            48.68), sixth(6, 33.63, 46.42), svnt(7, 37.47, 57.33), egth(
            8, 27.18, 56.27), nnth(9, 28.96, 50.84), birj(10,
            32.88, 59.22), tab(11, 38.08d, 46.3), T(12,
            35.68, 51.42), koh(13, 33.48, 48.35), rasht(14,
            37.3, 49.63), zeh(15, 29.5, 60.85), jan(16, 36.67,
            48.48), sari(17, 36.55, 53.1), smnn(18, 35.57, 53.38), sana(
            19, 35.3, 47.02), kord(20, 32.32, 50.85), razi(
            21, 29.62, 52.53), ghazi(22, 36.45, 50),ghum(23,
            34.65, 50.95), krj(24, 35.82, 50.97), kerm(25, 30.28,
            57.06), krmn(26, 34.32, 47.06), gurjan(27, 36.83,
            54.48), mshd(28, 34.3, 59.57), hmdn(29, 34.77,
            48.58), ysj(30, 30.82, 51.68), yaz(31, 31.90, 54.37);

    private int id;
    private Location location;

    LocationEnum(int id, double latitude, double longitude) {
        Location l = new Location("GPS");
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        this.location = l;
        this.id = id;

    }

    public int getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName(Context context) {
        return context.getResources().getStringArray(R.array.state_names)[this.id - 1];
    }
}

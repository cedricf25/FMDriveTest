package net.rncmobile.fmdrivetest.data.db.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.rncmobile.fmdrivetest.models.cells.IMyCell;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class CellRecorder implements Serializable {
    @Expose
    @SerializedName("id")
    @Id(autoincrement = true)
    protected Long _id;

    @Expose
    @Index
    @SerializedName("_support_id")
    @Property(nameInDb = "_support_id")
    protected int _support_id;

    @Expose
    @Index
    @SerializedName("_tech")
    @Property(nameInDb = "_tech")
    protected int _tech;

    @Expose
    @Index
    @SerializedName("_tech1")
    @Property(nameInDb = "_tech1")
    protected int _tech1 = -1;

    @Expose
    @Index
    @SerializedName("_tech2")
    @Property(nameInDb = "_tech2")
    protected int _tech2 = -1;

    @Expose
    @Index
    @SerializedName("_tech3")
    @Property(nameInDb = "_tech3")
    protected int _tech3 = -1;

    @Expose
    @SerializedName("_mcc")
    @Property(nameInDb = "_mcc")
    protected int _mcc;

    @Expose
    @SerializedName("_mnc")
    @Property(nameInDb = "_mnc")
    protected int _mnc;

    @Expose
    @Index
    @SerializedName("_lcid")
    @Property(nameInDb = "_lcid")
    protected long _lcid;

    @Expose
    @Index
    @SerializedName("_cid")
    @Property(nameInDb = "_cid")
    protected int _cid;

    @Expose
    @Index
    @SerializedName("_rnc")
    @Property(nameInDb = "_rnc")
    protected int _rnc;

    @Expose
    @Index
    @SerializedName("_psc")
    @Property(nameInDb = "_psc")
    protected int _psc;

    @Expose
    @Index
    @SerializedName("_psc1")
    @Property(nameInDb = "_psc1")
    protected int _psc1 = -1;

    @Expose
    @Index
    @SerializedName("_psc2")
    @Property(nameInDb = "_psc2")
    protected int _psc2 = -1;

    @Expose
    @Index
    @SerializedName("_psc3")
    @Property(nameInDb = "_psc3")
    protected int _psc3 = -1;

    @Expose
    @Index
    @SerializedName("_xrfcn")
    @Property(nameInDb = "_xrfcn")
    protected int _xrfcn = -1;

    @Expose
    @Index
    @SerializedName("_xrfcn1")
    @Property(nameInDb = "_xrfcn1")
    protected int _xrfcn1 = -1;

    @Expose
    @Index
    @SerializedName("_xrfcn2")
    @Property(nameInDb = "_xrfcn2")
    protected int _xrfcn2 = -1;

    @Expose
    @Index
    @SerializedName("_xrfcn3")
    @Property(nameInDb = "_xrfcn3")
    protected int _xrfcn3 = -1;

    @Expose
    @SerializedName("_lac")
    @Property(nameInDb = "_lac")
    protected int _lac;

    @Expose
    @Index
    @SerializedName("_lon")
    @Property(nameInDb = "_lon")
    protected double _lon;

    @Expose
    @Index
    @SerializedName("_lat")
    @Property(nameInDb = "_lat")
    protected double _lat;

    @Expose
    @Index
    @SerializedName("_altitude")
    @Property(nameInDb = "_altitude")
    protected double _altitude;

    @Expose
    @Index
    @SerializedName("_accurency")
    @Property(nameInDb = "_accurency")
    protected float _accurency;

    @Expose
    @Index
    @SerializedName("_speed")
    @Property(nameInDb = "_speed")
    protected float _speed;

    @Expose
    @SerializedName("_signal")
    @Property(nameInDb = "_signal")
    protected float _signal;

    @Expose
    @SerializedName("_signal1")
    @Property(nameInDb = "_signal1")
    protected float _signal1 = -1;

    @Expose
    @SerializedName("_signal2")
    @Property(nameInDb = "_signal2")
    protected float _signal2 = -1;

    @Expose
    @SerializedName("_signal3")
    @Property(nameInDb = "_signal3")
    protected float _signal3 = -1;

    @Expose
    @SerializedName("_battery")
    @Property(nameInDb = "_battery")
    protected int _battery;

    @Expose
    @Index
    @SerializedName("_date")
    @Property(nameInDb = "_date")
    protected String _date;

    @Expose
    @Index
    @SerializedName("_timestamp")
    @Property(nameInDb = "_timestamp")
    protected long _timestamp;

    @Expose
    @SerializedName("_sync")
    @Property(nameInDb = "_sync")
    private int _sync;

    static final long serialVersionUID = 44L;

    public CellRecorder() {
        _tech = -1;
        _mcc = 0;
        _mnc = 0;
        _lac = -1;
        _lon = 0.0;
        _lat = 0.0;
        _altitude = 0.0;
        _accurency = 0;
        _speed = 0;
        _signal = 0;
        _lcid = -1;
        _cid = -1;
        _psc = -2;
        _support_id = 0;
        _sync = 0;
        _timestamp = 0;
        _battery = 0;
    }

    public CellRecorder(IMyCell myCell, double lat, double lon, double altitude, float accurency, float speed, int batLevel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);

        _tech = myCell.getTechDetect();
        _mcc = myCell.getMcc();
        _mnc = myCell.getMnc();
        _lac = myCell.getXac();
        _lon = lon;
        _lat = lat;
        _altitude = altitude;
        _accurency = accurency;
        _speed = speed;
        _signal = myCell.getMainSignal();
        _lcid = myCell.getLcid();
        _cid = myCell.getCid();
        _rnc = myCell.getRnc();
        _psc = myCell.getPxx();
        _xrfcn = myCell.getDownLink();
        _date = sdf.format(new Date());
        _timestamp = new Date().getTime();
        _sync = 0;
        _battery = batLevel;
    }

    @Generated(hash = 1227406713)
    public CellRecorder(Long _id, int _support_id, int _tech, int _tech1, int _tech2, int _tech3, int _mcc, int _mnc, long _lcid,
            int _cid, int _rnc, int _psc, int _psc1, int _psc2, int _psc3, int _xrfcn, int _xrfcn1, int _xrfcn2, int _xrfcn3,
            int _lac, double _lon, double _lat, double _altitude, float _accurency, float _speed, float _signal, float _signal1,
            float _signal2, float _signal3, int _battery, String _date, long _timestamp, int _sync) {
        this._id = _id;
        this._support_id = _support_id;
        this._tech = _tech;
        this._tech1 = _tech1;
        this._tech2 = _tech2;
        this._tech3 = _tech3;
        this._mcc = _mcc;
        this._mnc = _mnc;
        this._lcid = _lcid;
        this._cid = _cid;
        this._rnc = _rnc;
        this._psc = _psc;
        this._psc1 = _psc1;
        this._psc2 = _psc2;
        this._psc3 = _psc3;
        this._xrfcn = _xrfcn;
        this._xrfcn1 = _xrfcn1;
        this._xrfcn2 = _xrfcn2;
        this._xrfcn3 = _xrfcn3;
        this._lac = _lac;
        this._lon = _lon;
        this._lat = _lat;
        this._altitude = _altitude;
        this._accurency = _accurency;
        this._speed = _speed;
        this._signal = _signal;
        this._signal1 = _signal1;
        this._signal2 = _signal2;
        this._signal3 = _signal3;
        this._battery = _battery;
        this._date = _date;
        this._timestamp = _timestamp;
        this._sync = _sync;
    }

    public void setAggreg1(IMyCell cell) {
        this._tech1 = cell.getTech();
        this._signal1 = cell.getMainSignal();
        this._xrfcn1 = cell.getDownLink();
        this._psc1 = cell.getPxx();
    }

    public void setAggreg2(IMyCell cell) {
        this._tech2 = cell.getTech();
        this._signal2 = cell.getMainSignal();
        this._xrfcn2 = cell.getDownLink();
        this._psc2 = cell.getPxx();
    }

    public void setAggreg3(IMyCell cell) {
        this._tech3 = cell.getTech();
        this._signal3 = cell.getMainSignal();
        this._xrfcn3 = cell.getDownLink();
        this._psc3 = cell.getPxx();
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int get_support_id() {
        return _support_id;
    }

    public void set_support_id(int _support_id) {
        this._support_id = _support_id;
    }

    public int get_tech() {
        return _tech;
    }

    public void set_tech(int _tech) {
        this._tech = _tech;
    }

    public int get_tech1() {
        return _tech1;
    }

    public void set_tech1(int _tech1) {
        this._tech1 = _tech1;
    }

    public int get_tech2() {
        return _tech2;
    }

    public void set_tech2(int _tech2) {
        this._tech2 = _tech2;
    }

    public int get_tech3() {
        return _tech3;
    }

    public void set_tech3(int _tech3) {
        this._tech3 = _tech3;
    }

    public int get_mcc() {
        return _mcc;
    }

    public void set_mcc(int _mcc) {
        this._mcc = _mcc;
    }

    public int get_mnc() {
        return _mnc;
    }

    public void set_mnc(int _mnc) {
        this._mnc = _mnc;
    }

    public long get_lcid() {
        return _lcid;
    }

    public void set_lcid(long _lcid) {
        this._lcid = _lcid;
    }

    public int get_cid() {
        return _cid;
    }

    public void set_cid(int _cid) {
        this._cid = _cid;
    }

    public int get_rnc() {
        return _rnc;
    }

    public void set_rnc(int _rnc) {
        this._rnc = _rnc;
    }

    public int get_psc() {
        return _psc;
    }

    public void set_psc(int _psc) {
        this._psc = _psc;
    }

    public int get_psc1() {
        return _psc1;
    }

    public void set_psc1(int _psc1) {
        this._psc1 = _psc1;
    }

    public int get_psc2() {
        return _psc2;
    }

    public void set_psc2(int _psc2) {
        this._psc2 = _psc2;
    }

    public int get_psc3() {
        return _psc3;
    }

    public void set_psc3(int _psc3) {
        this._psc3 = _psc3;
    }

    public int get_xrfcn() {
        return _xrfcn;
    }

    public void set_xrfcn(int _xrfcn) {
        this._xrfcn = _xrfcn;
    }

    public int get_xrfcn1() {
        return _xrfcn1;
    }

    public void set_xrfcn1(int _xrfcn1) {
        this._xrfcn1 = _xrfcn1;
    }

    public int get_xrfcn2() {
        return _xrfcn2;
    }

    public void set_xrfcn2(int _xrfcn2) {
        this._xrfcn2 = _xrfcn2;
    }

    public int get_xrfcn3() {
        return _xrfcn3;
    }

    public void set_xrfcn3(int _xrfcn3) {
        this._xrfcn3 = _xrfcn3;
    }

    public int get_lac() {
        return _lac;
    }

    public void set_lac(int _lac) {
        this._lac = _lac;
    }

    public double get_lon() {
        return _lon;
    }

    public void set_lon(double _lon) {
        this._lon = _lon;
    }

    public double get_lat() {
        return _lat;
    }

    public void set_lat(double _lat) {
        this._lat = _lat;
    }

    public double get_altitude() {
        return _altitude;
    }

    public void set_altitude(double _altitude) {
        this._altitude = _altitude;
    }

    public float get_accurency() {
        return _accurency;
    }

    public void set_accurency(float _accurency) {
        this._accurency = _accurency;
    }

    public float get_speed() {
        return _speed;
    }

    public void set_speed(float _speed) {
        this._speed = _speed;
    }

    public float get_signal() {
        return _signal;
    }

    public void set_signal(float _signal) {
        this._signal = _signal;
    }

    public float get_signal1() {
        return _signal1;
    }

    public void set_signal1(float _signal1) {
        this._signal1 = _signal1;
    }

    public float get_signal2() {
        return _signal2;
    }

    public void set_signal2(float _signal2) {
        this._signal2 = _signal2;
    }

    public float get_signal3() {
        return _signal3;
    }

    public void set_signal3(float _signal3) {
        this._signal3 = _signal3;
    }

    public int get_battery() {
        return _battery;
    }

    public void set_battery(int _battery) {
        this._battery = _battery;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public long get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(long _timestamp) {
        this._timestamp = _timestamp;
    }

    public int get_sync() {
        return _sync;
    }

    public void set_sync(int _sync) {
        this._sync = _sync;
    }
}

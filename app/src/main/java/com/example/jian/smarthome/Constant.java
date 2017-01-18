package com.example.jian.smarthome;

public class Constant {
	//IP地址
	public static String IP="192.168.0.109";
	//端口
	public static int port=4001;
	public static String CARD_ID="5A EF C6 04";
	public static String AREA_DATA="00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";//17位
	//寻卡
	public static final String FIND_CARD_CMD="aa bb 06 00 00 00 01 02 52 51";
	public static final String FIND_CARD_SUCCESS="aa bb 08 00 00 00 01 02 00 04 00 07";
	public static final String FIND_CARD_ERROR="aa bb 06 00 00 00 01 02 14 14";
	//读卡
	public static final String READ_CARD_CMD="AA BB 06 00 00 00 02 02 04 04";
	public static String READ_CARD_SUCCESS="AA BB 0A 00 00 00 02 02 00 5A EF C6 04 77";
	public static final String READ_CARD_ERROR="AA BB 06 00 00 00 02 02 0A 0A";
	public static String TAG="CASE";
	//光照度
	public static String SUN_IP= "192.168.0.103";
	public static int SUN_PORT=4001;
	public static String SUN_CHK= "01 03 00 2a 00 01 a5 c2";
	public static int SUN_NUM=1;
	public static int SUN_LEN=7;
	public static Integer sun=null;
	//温湿度
	public static String TEMHUM_IP= "192.168.0.100";
	public static int TEMHUM_PORT=4001;
	public static String TEMHUM_CHK= "01 03 00 14 00 02 84 0f";
	public static int TEMHUM_NUM=1;
	public static int TEMHUM_LEN=9;
	public static Integer tem=null;
	public static Integer hum=null;

	//pm2.5
	public static String PM25_IP= "192.168.0.101";
	public static int PM25_PORT=4001;
	public static String PM25_CHK= "01 03 00 58 00 01 05 d9";
	public static int PM25_NUM=1;
	public static int PM25_LEN=7;
	public static Integer pm25=null;

	//配置
	public static Integer time=500;

	//IP地址
	public static String CURTAINIP="192.168.0.108";
	//端口
	public static int CURTAINPort=4001;

	public static String CURTAIN_ON="01 10 00 4a 00 01 02 02 bb e9 29";
	public static String CURTAIN_OFF="01 10 00 4a 00 01 02 01 88 a9 cc";
	public static int CURTAIN_LEN=11;
	public static int CURTAIN_NUM=1;

	//风扇命令
	public static String FAN_IP="192.168.0.105";
	public static String FAN_ON="01 10 00 48 00 01 02 00 01 68 18";
	public static String FAN_OFF="01 10 00 48 00 01 02 00 02 28 19";
	public static int FAN_LEN=11;
	public static int FAN_NUM=1;

	//数码管
	public static String TUBE_CMD= "01 10 00 5e 00 02 04 12 11 03 17 62 9c";

	//IP端口
	public static String BODY_IP= "192.168.0.104";
	public static int BODY_PORT=4001;
	public static String TUBE_IP= "192.168.0.106";
	public static int TUBE_PORT=4001;
	public static Integer TUBE_VALUSE = 0;

	// IP地址
	public static String BUZZER_IP = "192.168.0.107";
	// 端口
	public static int BUZZER_PORT = 4001;
	// 返回长度
	public static final int BUZZER_LEN = 13;
	// 节点号
	public static final int BUZZER_NUM = 1;
	// 命令
	public static final String CLOSEALL_CMD = "01 10 00 5a 00 02 04 00 00 00 00 76 ec";
	public static final String BUZZER_CMD = "01 10 00 5a 00 02 04 01 00 00 00 77 10";
	public static final String RED_CMD = "01 10 00 5a 00 02 04 00 01 00 00 27 2c";
	public static final String GREEN_CMD = "01 10 00 5a 00 02 04 00 00 01 00 77 7c";
	public static final String BLUE_CMD = "01 10 00 5a 00 02 04 00 00 00 01 b7 2c";

	//IP地址
	public static String SMOKE_IP="192.168.0.102";

	//烟雾查询命令
	public static String SMOKE_CHK="01 03 00 34 00 01 c5 c4";
	public static int SMOKE_LEN=7;
	public static int SMOKE_NUM=1;
	public static Integer SMOKE;
}

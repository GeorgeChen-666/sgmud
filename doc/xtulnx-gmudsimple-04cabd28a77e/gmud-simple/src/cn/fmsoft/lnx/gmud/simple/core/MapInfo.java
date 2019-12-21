package cn.fmsoft.lnx.gmud.simple.core;

public class MapInfo {
	
	// 64
	static final int Map_in_room[] =  new int[]{
			1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 
			12, 13, 14, 15, 17, 20, 24, 25, 26, 27, 
			29, 30, 32, 33, 34, 35, 37, 38, 43, 45, 
			46, 47, 48, 49, 50, 52, 53, 55, 56, 58, 
			60, 61, 62, 63, 65, 66, 67, 68, 70, 71, 
			72, 74, 75, 76, 77, 78, 80, 81, 82, 84, 
			85, 86, 88, 90
		};

	// 92
	static final int Map_element_index[] = new int[] {
			0, 140, 176, 236, 296, 356, 436, 472, 536, 576, 
			728, 764, 792, 820, 848, 884, 948, 1004, 1056, 1232, 
			1392, 1412, 1464, 1572, 1752, 1804, 1832, 1884, 1920, 1976, 
			2052, 2080, 2256, 2300, 2336, 2372, 2416, 2460, 2480, 2492, 
			2616, 2712, 2792, 2916, 2936, 3112, 3164, 3224, 3284, 3304, 
			3356, 3392, 3460, 3524, 3552, 3824, 3860, 3896, 3936, 3952, 
			4128, 4156, 4184, 4228, 4248, 4396, 4440, 4484, 4508, 4552, 
			4648, 4676, 4712, 4728, 4928, 4988, 5032, 5084, 5144, 5204, 
			5384, 5452, 5528, 5572, 5660, 5720, 5780, 5840, 5864, 5876, 
			5916, 5952
		};

	// 92
	static final int Map_event_index[] = new int[] {
			0, 140, 176, 236, 296, 356, 436, 472, 536, 576, 
			728, 764, 792, 820, 848, 884, 948, 1004, 1056, 1232, 
			1392, 1412, 1464, 1572, 1752, 1804, 1832, 1884, 1920, 1976, 
			2052, 2080, 2256, 2300, 2336, 2372, 2416, 2460, 2480, 2492, 
			2616, 2712, 2792, 2916, 2936, 3112, 3164, 3224, 3284, 3304, 
			3356, 3392, 3460, 3524, 3552, 3824, 3860, 3896, 3936, 3952, 
			4128, 4156, 4184, 4228, 4248, 4396, 4440, 4484, 4508, 4552, 
			4648, 4676, 4712, 4728, 4928, 4988, 5032, 5084, 5144, 5204, 
			5384, 5452, 5528, 5572, 5660, 5720, 5780, 5840, 5864, 5876, 
			5916, 5952
		};
	
	static public class MAPINFO {
		int data[];
		int size;
	};
	
	static void GetMapElem(MAPINFO value, int id)
	{
		value.data = null;
		value.size = 0;
		int i1 = Map_element_index[id];
		int i2 = Map_element_index[id+1];
		
		byte[] data = Res.getarraydata(0, i1, i2);
		
		value.data = Res.convert(data, 0, data.length);
		value.size = value.data.length;
	}
	
	static void GetMapEvent(MAPINFO value, int id) {
		value.data = null;
		value.size = 0;
		int i1 = Map_event_index[id];
		int i2 = Map_event_index[id+1];
		
		byte[] data = Res.getarraydata(1, i1, i2);
		
		value.data = Res.convert(data, 0, data.length);
		value.size = value.data.length;	
	}
}

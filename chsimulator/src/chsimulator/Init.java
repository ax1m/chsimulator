package chsimulator;

public class Init {

	public static int AS;
	public static int CPS;
	public static int passes;
	public static boolean idle;
	public static boolean active;
	public static boolean hybrid;

	public static void main(String[] args) {

		Arguments argHandler = new Arguments("a,i,c#,p#", args);

		try {
			AS = Integer.parseInt(args[0]);
		} catch(NumberFormatException | IndexOutOfBoundsException e) {
			System.out.print("Enter amount of AS:\n> ");
			AS = Integer.parseInt(System.console().readLine());
		}

		try {
			CPS = argHandler.getInt('c');
		} catch(NullPointerException e) {
			CPS = 10;
		}

		try {
			passes = argHandler.getInt('p');
		} catch(NullPointerException e) {
			passes = 1;
		}

		idle = argHandler.getBool('i');
		active = argHandler.getBool('a');
		hybrid = !idle && !active;

		if(idle && active) {
			System.out.println("Error: Active and Idle flags both set to true.\nPlease try again.");
			return;
		}

		// Precalculating static data
		System.out.println("Calculating tables...");
		Tables.precalcHeroCost();
		Tables.precalcHeroDamage();
		Tables.precalcZoneHP();
		Tables.precalcExpAncients();
		System.out.println("Tables populated.");

		Optimizer opt = new Optimizer(AS);
		opt.suggest(AS/5, Transcension.solveChor(AS/5), Transcension.solvePhan(AS/5), AS/5, AS/5);
		opt.optimize();

		int xyl, chor, phan, borb, pony, build;
		if(passes >0) do {
			xyl = (int) Math.round(opt.midresult.Xyl);
			chor = (int) Math.round(opt.midresult.Chor);
			phan = (int) Math.round(opt.midresult.Phan);
			borb = (int) Math.round(opt.midresult.Borb);
			pony = (int) Math.round(opt.midresult.Pony);
			build = buildCost(xyl, chor, phan, borb, pony);

			opt = new Optimizer(AS);
			opt.suggest(xyl, chor, phan, borb, pony);
			opt.optimize();
		} while(AS != build || --passes > 0);

		if(opt.bestresult.Borb >= 1) System.out.println("Hail Borb!");
	}

	public static int chorCost(int lvl) {
		int res = 0;
		int i = 1;
		while(lvl > 10) {
			res += 10 * i++;
			lvl -= 10;
		}
		res += lvl * i;
		return res;
	}

	public static int phanCost(int lvl) {
		int res = 0;
		while(lvl > 0) {
			res += lvl--;
		}
		return res;
	}

	public static int buildCost(int xyl, int chor, int phan, int borb, int pony) {
		return xyl + chorCost(chor) + phanCost(phan) + borb + pony;
	}

}

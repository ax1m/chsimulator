package chsimulator;

public class Init {

	public static int AS;
	public static int CPS;
	public static boolean idle;
	public static boolean active;
	public static boolean hybrid;

	public static void main(String[] args) {

		Arguments argHandler = new Arguments("a,i,c#,s#", args);

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

		int xyl = (int) Math.round(opt.midresult.Xyl);
		int chor = (int) Math.round(opt.midresult.Chor);
		int phan = (int) Math.round(opt.midresult.Phan);
		int borb = (int) Math.round(opt.midresult.Borb);
		int pony = (int) Math.round(opt.midresult.Pony);
		int build = buildCost(xyl, chor, phan, borb, pony);

		opt = new Optimizer(AS);
		opt.suggest(xyl, chor, phan, borb, pony);
		opt.optimize();

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

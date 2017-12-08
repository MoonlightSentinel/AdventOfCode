import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day7 {
	
	public static void main(String[] args) {
		for (String s : allInputs) {
			final Program root = parseTree(s);
			
			System.out.println("Root: " + root.name + "\nExpected weight: " + find(root));
		}
	}
	
	public static int find(final Program root) {
		Map<Integer, List<Program>> counter = new HashMap<>();
		
		for(Program sub : root.holdingPrograms) {
			int res = find(sub);
			
			if(res != -1) return res;
			
			counter.computeIfAbsent(sub.getWeight(), ArrayList::new).add(sub);
		}
		
		switch(counter.size()) {
			case 0:
			case 1: return -1;
			case 2:
				int right = 0, wrong = 0;
				Program wrongProg =null;
				
				for(Map.Entry<Integer, List<Program>> entry : counter.entrySet()) {
					List<Program> matched = entry.getValue();
					
					if(matched.size() == 1) {
						wrong = entry.getKey();
						wrongProg = matched.get(0);
					} else {
						right = entry.getKey();
					}
				}
				return wrongProg.weight + (right - wrong);
			default:
				throw new RuntimeException("Something went wrong...");
		}
	}
	
	static class Program {		
		String name;
		int weight, combinedWeight = -1;
		ArrayList<String> pendingPrograms = new ArrayList<>();
		ArrayList<Program> holdingPrograms = new ArrayList<>();
		Program root = null;
		
		public int getWeight() {
			return combinedWeight != -1 ? combinedWeight		
				: (combinedWeight = 
				holdingPrograms.stream()
					.map(Program::getWeight)
					.reduce(weight, (i, last) -> i + last)
				);
		}

	}

	public static Program parseTree(final String list) {
		Map<String, Program> allPrograms = new HashMap<>();
		String cur;
		Scanner s = new Scanner(list);

		while(s.hasNext()) {
			Program p = new Program();
			
			// Name
			p.name = s.next();
			
			// Gewicht
			cur = s.next();
			p.weight = Integer.parseInt(cur.substring(1, cur.length() - 1));
			
			if(s.findInLine("->") != null) {
				while(true) {
					cur = s.next();
					
					if(cur.charAt(cur.length()-1) == ',') {
						addSubProgramm(cur.substring(0, cur.length() - 1), p, allPrograms);
					} else {
						addSubProgramm(cur, p, allPrograms);
						break;
					}
				}
			}
			
			allPrograms.put(p.name, p);
		}
		s.close();
		
		finishPending(allPrograms);
		
		return findRoot(allPrograms);
	} 

	private static void addSubProgramm(String name, Program p, Map<String, Program> allPrograms) {
		Program sub = allPrograms.get(name);
		
		if(sub == null) {
			p.pendingPrograms.add(name);
		} else {
			p.holdingPrograms.add(sub);
			sub.root = p;
		}
	}

	private static void finishPending(Map<String, Program> allPrograms) {
		for(Program p : allPrograms.values()) {
			for(String subName : p.pendingPrograms) {
				Program sub = allPrograms.get(subName);
				sub.root = p;
				p.holdingPrograms.add(sub);
			}
			
			p.pendingPrograms.clear();
		}
	}
	
	private static Program findRoot(Map<String, Program> allPrograms) {
		Program cur = allPrograms.values().iterator().next();
		
		while(cur.root != null) {
			cur = cur.root;
		}
		
		return cur;
	}
	
	@SuppressWarnings("unused")
	private static void print(final Program root, final int depth) {
		for(int i = 0; i < depth; i++) System.out.print('\t');
		System.out.println(root.name + " (" + root.combinedWeight + " [" + root.weight + "])");
		
		for(Program sub : root.holdingPrograms) {
			print(sub, depth +1);
		}
	}
	
	private static final String[] allInputs = {
			"pbga (66)\n" + 
			"xhth (57)\n" + 
			"ebii (61)\n" + 
			"havc (66)\n" + 
			"ktlj (57)\n" + 
			"fwft (72) -> ktlj, cntj, xhth\n" + 
			"qoyq (66)\n" + 
			"padx (45) -> pbga, havc, qoyq\n" + 
			"tknk (41) -> ugml, padx, fwft\n" + 
			"jptl (61)\n" + 
			"ugml (68) -> gyxo, ebii, jptl\n" + 
			"gyxo (61)\n" + 
			"cntj (57)\n",
			
			"occxa (60)\n" + 
			"kozpul (59) -> shavjjt, anujsv, tnzvo\n" + 
			"dotyah (138) -> tjhrdl, gdoxoc, aizbron\n" + 
			"jgsvfsl (30)\n" + 
			"eaxthh (5)\n" + 
			"dwklefi (82)\n" + 
			"rmlnt (199) -> epepfyw, dwrnlgg\n" + 
			"chrya (12)\n" + 
			"cvpimoj (164) -> eebyqij, vjipe, tdaxn\n" + 
			"pfmegn (934) -> netob, zsrao, xaslwy\n" + 
			"tqbqd (79) -> fjterk, qaryj\n" + 
			"slgoac (81)\n" + 
			"zoxjtzq (67)\n" + 
			"qtwvlxq (81)\n" + 
			"cpnoezu (81)\n" + 
			"lwhywr (639) -> qhqqpj, qrtdqj, mjhuaca\n" + 
			"kuhvyy (59)\n" + 
			"cidfsbr (81)\n" + 
			"wwjtzuf (206)\n" + 
			"mpflt (90)\n" + 
			"ysofby (54)\n" + 
			"ttktong (63)\n" + 
			"cryim (43)\n" + 
			"lgacwzo (17)\n" + 
			"kuyofu (81)\n" + 
			"fgont (20)\n" + 
			"ruqwlqw (99) -> ntnmzjx, nnxgmqu, eppyen\n" + 
			"lmbyxln (49)\n" + 
			"olynun (81) -> bpkcij, qutrc, itqaixc, jdbyghb\n" + 
			"zxjpf (35)\n" + 
			"oooma (50)\n" + 
			"jsanybb (39)\n" + 
			"bbycv (28) -> mevxgm, nxbeeb\n" + 
			"gfofc (38)\n" + 
			"ivspii (14)\n" + 
			"vpodq (191) -> dcxivs, fdpan, rhprie\n" + 
			"sggzhh (7376) -> phshj, dryrgj, bzujyh, ktvdag, suqkyqc\n" + 
			"sakgzat (77) -> lklfzh, mpzhwng\n" + 
			"oqhnzk (27) -> ekgzga, muzit, dnbpbn, sxyoft\n" + 
			"kkyby (181)\n" + 
			"hofab (98) -> zjdnsr, rxogya\n" + 
			"zwngnd (70)\n" + 
			"kqzwude (60)\n" + 
			"jkltwg (57) -> nsssqpp, bxoyo, hcdxy, mqreu\n" + 
			"chrpwez (55)\n" + 
			"cmsfw (85) -> sjrdmho, cuice, rqtiz, obcbd\n" + 
			"jtcxzs (31)\n" + 
			"wgquj (62) -> aotrzhg, acfeme\n" + 
			"cuice (44)\n" + 
			"ptaorpg (225) -> tnxvkeq, yiihd\n" + 
			"gpwzru (736) -> jyztkjv, rmlnt, tltte, oqnbzpa\n" + 
			"npmkva (30)\n" + 
			"zhhic (80)\n" + 
			"lrvcwu (73)\n" + 
			"uaerij (57) -> slauv, whckqd, mzrwlw, axfuq\n" + 
			"mguggym (31)\n" + 
			"pjpcyya (60)\n" + 
			"gxbbwl (96) -> pzkfyje, dpily, dhwffod, kbdjrob, pwfoj, pwqlro\n" + 
			"cdhed (28)\n" + 
			"eyttrr (34)\n" + 
			"jqyrqjo (15)\n" + 
			"oqzhbrd (34) -> dkktvs, fxzeacx, adrfscr\n" + 
			"scocac (62)\n" + 
			"ftkcs (91)\n" + 
			"jckhii (760) -> mbkcx, pgerst, qfhkrk, jdqxbag, qglqj\n" + 
			"trotwoi (71)\n" + 
			"mcgncp (48)\n" + 
			"ziwae (21)\n" + 
			"wriqytp (461)\n" + 
			"tnzvo (71)\n" + 
			"cyfkfo (67)\n" + 
			"zlrcjv (95)\n" + 
			"tqifa (99)\n" + 
			"zvkhqpa (30)\n" + 
			"nzdua (141)\n" + 
			"qjorpxg (242) -> dplsxrs, lbqwfgt\n" + 
			"yfjlrh (72) -> flwucgf, opdas\n" + 
			"xtaoa (97)\n" + 
			"mitlye (54)\n" + 
			"hnqjjhk (199) -> ruazjpw, cbhxz\n" + 
			"osaqp (85)\n" + 
			"isdxyo (68)\n" + 
			"oexuzjy (177) -> zranvyg, hutmzof\n" + 
			"mtinmla (71)\n" + 
			"xvffp (90) -> bkoard, oizyf\n" + 
			"aazgvmc (38513) -> dxoxxvk, zuahdoy, lopdq\n" + 
			"eydxsk (76) -> epjrvvp, rakwfhk, bwhot, lrymy\n" + 
			"qhosh (40) -> efozgh, ulxmu, ixtywxj, neagw\n" + 
			"bthky (132) -> liedl, ghicb\n" + 
			"kiuysw (62)\n" + 
			"neawpkj (207) -> evetqs, twemenc\n" + 
			"ncerbp (29)\n" + 
			"woioqeb (347)\n" + 
			"hbnsqgy (69) -> achjhb, ftqnpo\n" + 
			"tltte (127) -> ojhkvb, lgpewn\n" + 
			"qdlqy (57)\n" + 
			"iittzki (43)\n" + 
			"oqnbzpa (57) -> fxmyl, rptcitf\n" + 
			"mdhmr (71)\n" + 
			"kqnkzdl (76)\n" + 
			"dcmndzz (83)\n" + 
			"vkxicq (79)\n" + 
			"zvysgi (28)\n" + 
			"fxzeacx (87)\n" + 
			"nexgmr (24)\n" + 
			"wkxnnrg (90)\n" + 
			"rwvns (101) -> lgacwzo, zcslj, wopanki\n" + 
			"szphud (84) -> unbfqv, dvotce, rjgndzn\n" + 
			"dquww (98)\n" + 
			"pzkfyje (399)\n" + 
			"iwwbqle (16)\n" + 
			"jupsbj (32)\n" + 
			"dyibh (73)\n" + 
			"ngicn (30)\n" + 
			"smswtc (31) -> miyzuba, extlmn, kextbaa, zidgf\n" + 
			"qmghpah (358) -> vtdovx, acuce\n" + 
			"jyztkjv (155) -> iqccq, mcgncp\n" + 
			"lrcfe (96)\n" + 
			"tnxvkeq (24)\n" + 
			"noymo (11)\n" + 
			"wvmfzh (91)\n" + 
			"wjnhlj (132) -> whehan, lktfcet\n" + 
			"wzlfky (34)\n" + 
			"zpmslbn (291) -> ivspii, rbadc, wofzjph, obrfxlp\n" + 
			"eylle (9)\n" + 
			"iattz (29)\n" + 
			"ruzrr (67)\n" + 
			"bilrn (67)\n" + 
			"ouszgvx (82)\n" + 
			"xamoxye (11)\n" + 
			"wsbkos (75) -> hvaqke, fwtbaig\n" + 
			"xphjp (66)\n" + 
			"pxdnb (11729) -> jdmmnpg, xtcdf, qdcbmq, iztqqg, xluprc, kumzgqd\n" + 
			"trnzjj (219) -> wriqytp, amcrzt, fjhroj\n" + 
			"rxurj (5)\n" + 
			"boydbo (82)\n" + 
			"ptrqmgy (74)\n" + 
			"ioobi (197)\n" + 
			"slauv (65)\n" + 
			"ksqwp (142) -> kjjzo, qaxlov\n" + 
			"qnfxh (125) -> ihqhmp, kqnkzdl\n" + 
			"klpjjvg (44)\n" + 
			"wbqmyt (304)\n" + 
			"qfkkwc (428) -> qlkwu, xxgbjxv, zyzjfr\n" + 
			"qaryj (52)\n" + 
			"cvyof (32)\n" + 
			"tmeconw (77)\n" + 
			"eavzu (53)\n" + 
			"jmfvn (21)\n" + 
			"dvxkr (58)\n" + 
			"lsckdh (39) -> wdmqsw, mmwijic, dhsqdfu, sovbjm\n" + 
			"uownj (12) -> wqdviv, ctmydr, pxdnb, qipooo, aazgvmc, eidmwnu\n" + 
			"kbdjrob (237) -> uwlec, mranee\n" + 
			"xuuboqp (39)\n" + 
			"ggapah (22) -> occxa, fcila, kqzwude\n" + 
			"dmpmj (41)\n" + 
			"icpczr (16)\n" + 
			"aztlqe (70)\n" + 
			"rvjfiqd (61)\n" + 
			"xslcw (61) -> fleler, mslwcef, qybci\n" + 
			"txuqarh (1083) -> jgxxaah, bjdps, vyjyqm\n" + 
			"gmlyf (76)\n" + 
			"wqdviv (22436) -> wwfsfm, gxxrr, sggzhh\n" + 
			"xxkbo (156) -> dkpebdn, hepoax, qgaal, jcpaww\n" + 
			"mshtzph (85)\n" + 
			"cbprshs (10) -> lpjyqfd, gimjvji\n" + 
			"hcdxy (68)\n" + 
			"brekp (70)\n" + 
			"bjdps (260) -> kyfjsn, dkdttm\n" + 
			"ycsha (30)\n" + 
			"bxvht (65)\n" + 
			"hicwq (98)\n" + 
			"zrdvbl (71)\n" + 
			"agvef (57)\n" + 
			"hvaqke (25)\n" + 
			"quayoh (57)\n" + 
			"nbppbiy (48)\n" + 
			"mleek (90) -> lwisav, iatxfon\n" + 
			"axfuq (65)\n" + 
			"gtzfc (73)\n" + 
			"txelahj (81)\n" + 
			"iclgmrd (58)\n" + 
			"pfkkpf (69)\n" + 
			"kiiwvl (63)\n" + 
			"upnvuf (98) -> gtosn, mpflt, ahdjfhr\n" + 
			"wsmdf (66)\n" + 
			"avfse (71)\n" + 
			"cfnxaxh (176) -> zpilz, bfqgrx\n" + 
			"kvxtoa (80)\n" + 
			"gsqag (55)\n" + 
			"nnrme (32)\n" + 
			"sovbjm (77)\n" + 
			"ihqhmp (76)\n" + 
			"xptngy (76) -> xgfnh, pedyx\n" + 
			"qdufhxj (120)\n" + 
			"jvvrtrv (54)\n" + 
			"ldjcv (31)\n" + 
			"hiunh (162) -> fnnaq, ihesljo\n" + 
			"ywlwppw (107) -> vkmmhj, rtrqyt, jnmhgn\n" + 
			"nnwvn (81)\n" + 
			"zpilz (24)\n" + 
			"puqymd (183)\n" + 
			"cxndnyb (73)\n" + 
			"naxvkt (178) -> mguggym, ccodif\n" + 
			"eprwoep (93)\n" + 
			"mjatmj (83) -> wwumy, rvvrxj, aroct\n" + 
			"psnhw (81)\n" + 
			"cwxpvg (93)\n" + 
			"kdcbtyf (317)\n" + 
			"attvd (238) -> iznrffp, znksd\n" + 
			"qtlyvzn (32)\n" + 
			"ztowevm (75)\n" + 
			"nxbeeb (84)\n" + 
			"jdmmnpg (5564) -> dagpv, wkvfo, jrphokq\n" + 
			"cocpc (53)\n" + 
			"seehj (74)\n" + 
			"kphqbqy (872) -> cqjena, mwtcdof\n" + 
			"lwmlmt (85)\n" + 
			"mmwijic (77)\n" + 
			"rtrbpze (337) -> xlhtsn, hyvee, cyxpp\n" + 
			"ihesljo (78)\n" + 
			"rbadc (14)\n" + 
			"otrnol (98)\n" + 
			"ahjzy (134) -> faysq, zirepil\n" + 
			"rvzbog (53) -> dcmndzz, viojqk\n" + 
			"zzqucm (96) -> thnxcx, lelkjof, cpnoezu, qtwvlxq\n" + 
			"xphdneu (39)\n" + 
			"rcuauq (78)\n" + 
			"zyzjfr (226)\n" + 
			"lnezc (80)\n" + 
			"fnsol (28)\n" + 
			"iljnbiz (228) -> ziwae, tuylou, meecdj\n" + 
			"qlkwu (86) -> aztlqe, knmyjju\n" + 
			"uyxck (73)\n" + 
			"mfgah (83)\n" + 
			"iznrffp (40)\n" + 
			"ohxkp (87)\n" + 
			"soypqq (80)\n" + 
			"jnmhgn (83)\n" + 
			"vbjrc (241) -> iattz, ncerbp, yzbem\n" + 
			"obsfib (272)\n" + 
			"xgfnh (65)\n" + 
			"hjddl (13)\n" + 
			"gvtpttw (72)\n" + 
			"xbuelds (9)\n" + 
			"fmtveft (104) -> mmmqt, wkxnnrg\n" + 
			"txexr (28)\n" + 
			"kjtclbz (68) -> ruzrr, bqqbtuu\n" + 
			"dtnlyc (98)\n" + 
			"flmcu (66)\n" + 
			"gbikgsz (250)\n" + 
			"jdsfjkf (52) -> kyqidwo, ysyjko, tihmp, urcmmqc, grvssv\n" + 
			"fbtok (42)\n" + 
			"atngvo (344) -> dlhnqsu, ayrzzu\n" + 
			"dwfoa (87)\n" + 
			"achjhb (23)\n" + 
			"swgnd (96)\n" + 
			"piedp (337) -> eydxsk, wbqmyt, xmdpfk\n" + 
			"vzmxrqs (256) -> fduhgla, bysqw\n" + 
			"lxeit (84) -> npejje, yhkfv, fbtok, eksvho\n" + 
			"fsznv (96)\n" + 
			"uvekre (93)\n" + 
			"eykvd (213) -> scgjs, pusfms\n" + 
			"aizbron (27)\n" + 
			"hekaxal (251) -> xamfydv, nnrme, qzhlkec\n" + 
			"lbjvtj (19) -> isdxyo, ahhobtu, vkvdnam, fasqn\n" + 
			"qtaka (298) -> trwmicb, kdcbtyf, uaerij\n" + 
			"wdcers (12)\n" + 
			"psftlm (110) -> zoxjtzq, gawxacm\n" + 
			"hbxquoz (72)\n" + 
			"iyivq (174) -> ztzerr, nalot\n" + 
			"ozynj (11)\n" + 
			"qngdf (93)\n" + 
			"rtpusqx (1731) -> awywkmu, cbprshs, jpfcy\n" + 
			"vfmuj (95)\n" + 
			"qutrc (292)\n" + 
			"xcxroor (16)\n" + 
			"cxcibc (12)\n" + 
			"hcfghl (31) -> rnoivcb, swgnd, fsznv\n" + 
			"pnbrg (8558) -> pfmegn, mevgyhq, bhgzfhp\n" + 
			"yiaxyi (29)\n" + 
			"lktfcet (10)\n" + 
			"psesrd (18)\n" + 
			"hutmzof (50)\n" + 
			"ddzvqkr (1020) -> asyku, yfjlrh, naxvkt\n" + 
			"rbbtkcf (21)\n" + 
			"ctmydr (94) -> eiqfjh, swiyje, pnbrg, vseri, dbcevsv\n" + 
			"tkzjfkq (47)\n" + 
			"gvrmz (60)\n" + 
			"ktvnccr (53)\n" + 
			"nglegc (105) -> sdamh, tmonbh\n" + 
			"ackza (6)\n" + 
			"qipooo (46244) -> udaqlo, eltxjp, locrn\n" + 
			"trczg (140) -> eylle, qbxat\n" + 
			"mbljzmm (370) -> sorgx, bxakn, itokb\n" + 
			"oycjqok (79) -> nmdgz, pyozvr, bljxv\n" + 
			"obcbd (44)\n" + 
			"unhzsf (46) -> jkltwg, twbmbv, cedcjza, xywrttu\n" + 
			"cdubxqs (73)\n" + 
			"soweon (308) -> iwwbqle, xcxroor, icpczr\n" + 
			"jyavyin (455) -> nwzrdt, gbikgsz, hhoafz, iyivq\n" + 
			"xfqbdst (98) -> npzfhkv, hcfghl, neawpkj, uiecu, cijsvr, wucysx, dsgkaw\n" + 
			"ysphvx (12)\n" + 
			"pukgrz (121) -> opcszf, hvvlhe\n" + 
			"dvotce (253) -> kiiwvl, ttktong\n" + 
			"nxphajd (78) -> ynoxdm, bqvid\n" + 
			"twbmbv (45) -> kuolv, yvhgp, clmdi, sbwfpa\n" + 
			"yhkxud (140) -> xlajoju, yiaxyi\n" + 
			"eckrps (63)\n" + 
			"xtnctiv (275) -> hnqjjhk, lqsjdu, mfvzrxh\n" + 
			"wofzjph (14)\n" + 
			"nvbav (31)\n" + 
			"xamfydv (32)\n" + 
			"wxpae (180) -> vlmtckh, drxfuqd, viaivun\n" + 
			"xiiwbs (669) -> tptuf, loyiwus, pfluh, bchbcjc, qjorpxg, guxwpqu\n" + 
			"slbbde (94) -> agexcnl, wxscr\n" + 
			"pubnb (98)\n" + 
			"qayhfqp (81)\n" + 
			"apjgsl (232) -> bpuuio, qpqwnti\n" + 
			"ucodfmw (18)\n" + 
			"fmwgpf (69)\n" + 
			"sbwfpa (71)\n" + 
			"mvwvgtc (60)\n" + 
			"yykvyt (96)\n" + 
			"dycdyx (76)\n" + 
			"itnlnpw (87)\n" + 
			"klgfmp (121) -> spyey, qrzjxx, vtkkzt\n" + 
			"bchbcjc (205) -> emndhg, otkntx, tvhttj\n" + 
			"rtrqyt (83)\n" + 
			"bdrgk (1340) -> jwpff, mvwvgtc\n" + 
			"qadmk (18)\n" + 
			"efqeq (30)\n" + 
			"pyhrwp (61)\n" + 
			"jdqxbag (45) -> wzcrtz, endts, aajml, bprczj\n" + 
			"wngzj (98)\n" + 
			"ggrzmj (66)\n" + 
			"lbqwfgt (37)\n" + 
			"vahqnf (79)\n" + 
			"pktrea (114) -> fmrcz, cdubxqs, dlkhgy, enumule\n" + 
			"hyhkb (89) -> trotwoi, mtwod\n" + 
			"dodxloj (57)\n" + 
			"gizfx (79)\n" + 
			"tyvrby (49) -> kgifnwx, wvmfzh\n" + 
			"comkkpi (79)\n" + 
			"ozkxsak (74)\n" + 
			"daznpq (29)\n" + 
			"ysvmli (91)\n" + 
			"wonidli (41) -> qogcxh, mkthfc, eemtac, kuhvyy\n" + 
			"wbpqtn (121) -> nvbav, yqhlag\n" + 
			"vhlyct (31)\n" + 
			"bcknuwr (145) -> zsgntfb, tyueun\n" + 
			"qbqdcm (60)\n" + 
			"vseri (5387) -> mekxoeo, aavyxnd, qtaka, piedp, uegzzq, olynun\n" + 
			"sorgx (32) -> qwmqr, jgsvfsl, npmkva\n" + 
			"geexh (867) -> apyvj, idmwnxg, hgirpmg, yhkxud\n" + 
			"cfwjs (57)\n" + 
			"xaslwy (133) -> sfqps, ossumc\n" + 
			"azowpjp (2022) -> kkyby, sakgzat, avsqfag\n" + 
			"urarg (53)\n" + 
			"dxzrqa (28)\n" + 
			"obrfxlp (14)\n" + 
			"bxiroo (17)\n" + 
			"xkwuq (78)\n" + 
			"msnnf (265) -> bxvht, jtnoqx\n" + 
			"uiecu (319)\n" + 
			"atfeuv (60) -> snwwnem, ycsha\n" + 
			"gimjvji (95)\n" + 
			"buhyicq (11)\n" + 
			"ylavfs (154) -> nbarshr, osaqp\n" + 
			"yjvso (18)\n" + 
			"zmkno (72)\n" + 
			"nvold (2040) -> jssuppj, bfmwj\n" + 
			"ruazjpw (39)\n" + 
			"mekxoeo (1133) -> iclgmrd, dvxkr\n" + 
			"xspgdc (56)\n" + 
			"yjhyb (73)\n" + 
			"yptmfb (259) -> qtsfwnv, gksdobg, fuxir\n" + 
			"dxoxxvk (643) -> ozafzy, flsqqw, yfxlqu\n" + 
			"peiiks (11)\n" + 
			"gcqeyqn (84)\n" + 
			"ygqakoh (60)\n" + 
			"rjgndzn (201) -> ncxfef, hlkerz\n" + 
			"ynnefdj (88)\n" + 
			"tbsrvbd (72)\n" + 
			"awywkmu (60) -> veojykb, brekp\n" + 
			"npyxpyc (48)\n" + 
			"tndlco (2699) -> ddzvqkr, gpwzru, oqmliz, oxada\n" + 
			"vpapi (28)\n" + 
			"hlkerz (89)\n" + 
			"pzmvwhg (77)\n" + 
			"jtmbf (93)\n" + 
			"mcwhhll (92)\n" + 
			"mevxgm (84)\n" + 
			"agexcnl (41)\n" + 
			"guxwpqu (199) -> jsanybb, wlxgck, jtzidh\n" + 
			"oyjliid (63)\n" + 
			"oqmliz (32) -> fxzmev, axhgfu, gvilf, upzpfu, izlpj, psftlm, xmynm\n" + 
			"pedyx (65)\n" + 
			"bjdabjc (37) -> vlanmkf, efqeq\n" + 
			"hbscmk (62)\n" + 
			"ljmxejh (87)\n" + 
			"hbtqsvg (75)\n" + 
			"qcmpct (29)\n" + 
			"ejltcdq (98)\n" + 
			"jcpaww (353) -> yxmsys, nkriu\n" + 
			"rtttief (38)\n" + 
			"uqrnq (195) -> qadmk, xcyvydj\n" + 
			"ouzekb (2145) -> wfvbe, wxzuuax, hbnsqgy\n" + 
			"aputyq (6)\n" + 
			"sxukoj (65)\n" + 
			"wfvbe (101) -> robfda, ddzoj\n" + 
			"mftbcdj (196) -> cqehja, mhxta\n" + 
			"chcmfns (24) -> mfgah, lzzakg, eomcn, jztiyg\n" + 
			"gvywrbk (45)\n" + 
			"epepfyw (26)\n" + 
			"eidmwnu (35522) -> vdrssx, tndlco, vmlot\n" + 
			"fxlfsh (202)\n" + 
			"jwpff (60)\n" + 
			"rqtiz (44)\n" + 
			"nranogq (82)\n" + 
			"nbarshr (85)\n" + 
			"mdqgbt (27)\n" + 
			"xxgbjxv (106) -> tkvey, qbqdcm\n" + 
			"bpkcij (244) -> nexgmr, vlgck\n" + 
			"lrndlu (8)\n" + 
			"zxiyxq (49) -> oqhnzk, ralcb, cqhzg, msnnf, vckvw\n" + 
			"qtvfi (83)\n" + 
			"zciggcz (14) -> ktvnccr, hdlwni\n" + 
			"ncovrwd (16)\n" + 
			"znbuoor (65)\n" + 
			"dnbpbn (92)\n" + 
			"ioioi (187)\n" + 
			"ffzmh (25)\n" + 
			"zxorl (73)\n" + 
			"vlgmhuv (251) -> smswtc, gmsvr, ekrfpes\n" + 
			"cyxpp (28)\n" + 
			"dlrbh (29) -> ejltcdq, prnxzn, ywdzbpm, zwapr\n" + 
			"ppltn (111) -> psesrd, atjml, ictjzx, xwzln\n" + 
			"mfpfqpd (73)\n" + 
			"unbfqv (59) -> soypqq, xcdpn, kvxtoa, qekfk\n" + 
			"dlkhgy (73)\n" + 
			"hhsaqkq (23)\n" + 
			"kuolv (71)\n" + 
			"moxllkd (86)\n" + 
			"thnxcx (81)\n" + 
			"lhfopq (49)\n" + 
			"zgfpv (95)\n" + 
			"eiwbgkd (15)\n" + 
			"xoexpuv (100) -> eazmfh, xzwdltr, qvrhd\n" + 
			"ajptvhc (96)\n" + 
			"oxyxxv (273)\n" + 
			"fcila (60)\n" + 
			"bfqgrx (24)\n" + 
			"cuopz (15)\n" + 
			"ernwmw (15)\n" + 
			"ujdaj (85)\n" + 
			"pxggj (92)\n" + 
			"tvtbrol (220) -> zxjpf, raimv\n" + 
			"hgirpmg (172) -> pzzlwt, lwkib\n" + 
			"sgfes (35)\n" + 
			"aotrzhg (95)\n" + 
			"qzuanqw (125) -> vcikq, vhlyct\n" + 
			"wtlxw (95)\n" + 
			"opdas (84)\n" + 
			"lpzseo (201) -> fvcnod, aputyq, fchob\n" + 
			"qgaal (385) -> jlbihbs, dmpmj\n" + 
			"sfqps (18)\n" + 
			"nftuawm (77) -> unpta, mdqgbt, lwhlpt\n" + 
			"djkfqo (50)\n" + 
			"hvjak (55)\n" + 
			"kextbaa (93)\n" + 
			"ztzerr (38)\n" + 
			"shavjjt (71)\n" + 
			"vnpro (313) -> wjnhlj, uodsyhs, rwvns\n" + 
			"qvrhd (32)\n" + 
			"lnxnld (16)\n" + 
			"klicwmt (65)\n" + 
			"scueow (18)\n" + 
			"gbaxyxf (79)\n" + 
			"mslwcef (70) -> wwpgr, wvvuyku\n" + 
			"eemtac (59)\n" + 
			"icdqutb (88)\n" + 
			"bataqig (78)\n" + 
			"xtcdf (6041) -> kphqbqy, wxpae, rdbxsg\n" + 
			"atktbij (146) -> akgtc, xqdno, ioioi, soxeud, piplyy, nglegc, qzuanqw\n" + 
			"nqzhi (83)\n" + 
			"fegzjc (99)\n" + 
			"hcltwvs (81) -> wjkajc, lmbyxln, ukktl, epeudep\n" + 
			"dbaczh (107) -> sszivvi, xbuelds\n" + 
			"ralcb (239) -> uyfpwa, rcuauq\n" + 
			"viojqk (83)\n" + 
			"gizpa (54)\n" + 
			"cdcpur (60)\n" + 
			"vjipe (17)\n" + 
			"njbklo (50)\n" + 
			"jhbzqh (98)\n" + 
			"uoscg (49) -> tgvowq, klicwmt\n" + 
			"mnlkn (91)\n" + 
			"xmzreq (5)\n" + 
			"bqvid (49)\n" + 
			"ddzoj (7)\n" + 
			"mevgyhq (1309) -> wsmdf, tcbpl\n" + 
			"qwibwv (86)\n" + 
			"kzaee (29)\n" + 
			"tqlbxbk (206)\n" + 
			"kzgvdvy (470)\n" + 
			"zokhh (85)\n" + 
			"qvasqa (292) -> qtlyvzn, yfvrd\n" + 
			"cedcjza (221) -> fgouoot, bdbfwjx\n" + 
			"immwi (50)\n" + 
			"iqccq (48)\n" + 
			"eppyen (238) -> hmxve, boobyu\n" + 
			"ryemzzp (119) -> jupsbj, cjgfncf\n" + 
			"mpzhwng (52)\n" + 
			"lrvkkp (81) -> fmwgpf, pfkkpf\n" + 
			"xwrkqj (64)\n" + 
			"swiyje (10574) -> oycjqok, vnpro, xslcw\n" + 
			"jgxxaah (344) -> yjvso, oaylpg, vnjtmkj, njfgh\n" + 
			"ignnpoe (1037) -> tpuff, nzdua, giidwph\n" + 
			"ljrzagq (104) -> aouceoo, jtcxzs, ldjcv\n" + 
			"pusfms (30)\n" + 
			"evetqs (56)\n" + 
			"tuctl (87)\n" + 
			"jxgoaqy (80)\n" + 
			"vnjtmkj (18)\n" + 
			"nmdgz (144) -> ypvmcm, ddaxciv\n" + 
			"zlfmdxz (12)\n" + 
			"yxmsys (57)\n" + 
			"tvctwy (50)\n" + 
			"whehan (10)\n" + 
			"dagpv (786) -> gkgab, bjdabjc, rbgqim\n" + 
			"ulxmu (34)\n" + 
			"drpftnv (87)\n" + 
			"yzbem (29)\n" + 
			"raimv (35)\n" + 
			"cdstxnu (238) -> vngxw, pqnhasu\n" + 
			"lnbgem (25)\n" + 
			"lwzvpvx (53)\n" + 
			"akgtc (91) -> fqeguqv, clcdcgr, pzozfud\n" + 
			"oolchg (202) -> rvjfiqd, pyhrwp\n" + 
			"eajgpo (51)\n" + 
			"wxzuuax (103) -> ackza, kimuc\n" + 
			"mgzhv (5)\n" + 
			"sggqt (80)\n" + 
			"tyueun (58)\n" + 
			"neagw (34)\n" + 
			"cbxzq (48)\n" + 
			"zranvyg (50)\n" + 
			"msgckc (406)\n" + 
			"gohnrwt (75)\n" + 
			"ksfylmr (149) -> dmzvtf, xfzgdj\n" + 
			"lwisav (43)\n" + 
			"ktvdag (393) -> zowwvl, yhrhiyx, idsfdf, zyukres\n" + 
			"vlbvqd (80)\n" + 
			"nwzrdt (150) -> tvctwy, immwi\n" + 
			"tptuf (28) -> hbxquoz, zmkno, gvtpttw, tbsrvbd\n" + 
			"zcslj (17)\n" + 
			"qrtdqj (12) -> cwxpvg, jtmbf\n" + 
			"svcgs (140) -> jpxxgq, scueow\n" + 
			"fdxcyr (75)\n" + 
			"yiihd (24)\n" + 
			"bkyrrr (7)\n" + 
			"izlpj (68) -> nukmm, kalidnk\n" + 
			"vopvppg (34)\n" + 
			"ossumc (18)\n" + 
			"fduhgla (56)\n" + 
			"eggpdp (73)\n" + 
			"yhkfv (42)\n" + 
			"xywrttu (285) -> peiiks, noymo, chttax, knnre\n" + 
			"dsgvsd (138)\n" + 
			"idsfdf (224) -> mgzhv, rsqkxid\n" + 
			"loyiwus (182) -> bilrn, cyfkfo\n" + 
			"ajcedg (88)\n" + 
			"dyhpnf (75)\n" + 
			"jtnoqx (65)\n" + 
			"ptzhczg (45)\n" + 
			"mewzj (5)\n" + 
			"fuiwxoi (50)\n" + 
			"unnwuw (53)\n" + 
			"hnuakg (129) -> pbvhscz, msgckc, qpjosh, fhcjbaf, bhznh, pktrea\n" + 
			"pfnew (14) -> sygqg, hkawy\n" + 
			"tptuy (44)\n" + 
			"akrhumm (86)\n" + 
			"yfoeg (30) -> dyibh, lrvcwu\n" + 
			"bwhot (57)\n" + 
			"yejlp (54)\n" + 
			"fgouoot (54)\n" + 
			"eltxjp (13) -> istfw, zxiyxq, xxkbo\n" + 
			"kimuc (6)\n" + 
			"dkdttm (78)\n" + 
			"dsfeott (74)\n" + 
			"xdyhig (134) -> ckhdiiu, djkfqo, qxmwtlw\n" + 
			"scgjs (30)\n" + 
			"hlpvxds (15)\n" + 
			"lipveyk (25)\n" + 
			"bqqbtuu (67)\n" + 
			"ahdjfhr (90)\n" + 
			"eomcn (83)\n" + 
			"mmmqt (90)\n" + 
			"hdlwni (53)\n" + 
			"vffjkzp (8) -> kjmhqa, oreple, ttquuz\n" + 
			"hdptwar (34)\n" + 
			"qihaqhe (81)\n" + 
			"znksd (40)\n" + 
			"mjhuaca (48) -> hbtqsvg, gohnrwt\n" + 
			"mzrwlw (65)\n" + 
			"rnoivcb (96)\n" + 
			"yctfou (254) -> hxftzw, sgfes\n" + 
			"wgzdoiy (81)\n" + 
			"tfrqfm (99)\n" + 
			"acxxafw (564) -> eevawfz, cfnxaxh, ktrpmw, axrvqtb\n" + 
			"jbnhonh (86)\n" + 
			"dhsqdfu (77)\n" + 
			"kjkxg (39)\n" + 
			"wucysx (319)\n" + 
			"dcpesz (87)\n" + 
			"gkeumf (83)\n" + 
			"ydhtgb (50) -> xdgvik, uyxck\n" + 
			"tymjf (59)\n" + 
			"tsvpfin (32)\n" + 
			"rbusgi (5) -> lwmlmt, ipzdtst\n" + 
			"nnxgmqu (236) -> zrdvbl, ebacdan\n" + 
			"fhcjbaf (253) -> gmgscd, eajgpo, ibdxfru\n" + 
			"njfgh (18)\n" + 
			"zvoxbcv (370) -> dixbd, uvkiha, yfoeg, svcgs, xdgfor, jlxfw, mleek\n" + 
			"pqkwc (8)\n" + 
			"vkvdnam (68)\n" + 
			"jqiyk (67)\n" + 
			"mypnwe (60)\n" + 
			"tcbpl (66)\n" + 
			"endts (79)\n" + 
			"kbmkrfb (270) -> iittzki, cryim\n" + 
			"ajafdiv (38) -> tymjf, xagcn, pebqce\n" + 
			"pnslx (49)\n" + 
			"zuahdoy (14) -> gsqcet, mfzpvpj, ygmtia, nvold\n" + 
			"kukwwup (12)\n" + 
			"lpjyqfd (95)\n" + 
			"ajllsx (77)\n" + 
			"axrvqtb (160) -> caqpd, slgdz, vjhxwan, ncovrwd\n" + 
			"ouslph (84)\n" + 
			"cqehja (38)\n" + 
			"cqmnmp (82)\n" + 
			"oeddmx (56)\n" + 
			"tpuff (36) -> deizqho, dgcaijl, dwnqi\n" + 
			"gvilf (82) -> jvvrtrv, gizpa, qvrlbp\n" + 
			"pyozvr (68) -> mitlye, lavsfcf, yxtmb\n" + 
			"rysqk (161) -> yjslt, ernwmw, jqyrqjo\n" + 
			"piplyy (159) -> iirqqkc, gbshnls\n" + 
			"fdpan (26)\n" + 
			"xjvzuax (73)\n" + 
			"rsqkxid (5)\n" + 
			"tihmp (58) -> cjamvug, ouslph, tzvye\n" + 
			"vfclsgp (53)\n" + 
			"irduh (33) -> nnnkqg, fdxcyr\n" + 
			"mbkcx (163) -> lsdlhcv, flmcu, xphjp\n" + 
			"vptwcy (1561) -> pnslx, lhfopq\n" + 
			"ddlrr (99) -> dlrbh, iothmz, rtrbpze\n" + 
			"foooidm (10) -> btvpas, yykvyt\n" + 
			"xuenhje (83)\n" + 
			"oreple (35)\n" + 
			"zyukres (88) -> hcason, wwzmxpr\n" + 
			"qsxkcs (23)\n" + 
			"yjcjjwh (256) -> ouszgvx, boydbo\n" + 
			"wopanki (17)\n" + 
			"txtsesn (39)\n" + 
			"lgooafx (67)\n" + 
			"csjtsvu (231) -> zvkhqpa, leoalr\n" + 
			"tkvey (60)\n" + 
			"bkoard (97)\n" + 
			"lxdnpno (28) -> eykrgl, ptrqmgy, ozkxsak, seehj\n" + 
			"kyqidwo (124) -> kiuysw, hbscmk, lpxod\n" + 
			"zirepil (69)\n" + 
			"okkry (64)\n" + 
			"qogcxh (59)\n" + 
			"xbxsa (98)\n" + 
			"xxlmsg (29)\n" + 
			"xmdpfk (304)\n" + 
			"ncxfef (89)\n" + 
			"drxfuqd (82) -> fyqrdda, lcldjzf\n" + 
			"ntnmzjx (30) -> dcpesz, tuctl, itnlnpw, fvoilc\n" + 
			"wwhwhlh (49)\n" + 
			"nkriu (57)\n" + 
			"bhgzfhp (31) -> kzgvdvy, oasngox, wqwckx\n" + 
			"eughxu (89)\n" + 
			"zzxvkw (175) -> zvysgi, xnuiprc\n" + 
			"zcxajvs (47)\n" + 
			"fzffa (15)\n" + 
			"mfzpvpj (604) -> ktrbnu, cmsfw, xjitfcr, nrukeq, tfofygr, bcknuwr\n" + 
			"azchhq (38)\n" + 
			"fjterk (52)\n" + 
			"hbummr (98)\n" + 
			"jsezaqz (84)\n" + 
			"qbsauln (555) -> vpodq, prqstpz, hxyfrr\n" + 
			"orvqjsu (13)\n" + 
			"qomla (67)\n" + 
			"nmgdw (20)\n" + 
			"tziwukl (71) -> iknffv, lzpis, ktlvla\n" + 
			"tlfhbr (320) -> kwnsatz, njbklo\n" + 
			"rbbrmfz (58) -> mfpfqpd, yjhyb, eggpdp\n" + 
			"qzhlkec (32)\n" + 
			"emndhg (37)\n" + 
			"gdmsj (345) -> rvzbog, lrvkkp, lpzseo, dotyah\n" + 
			"rfzkn (206) -> slgoac, qayhfqp\n" + 
			"kaswwui (67) -> woioqeb, hekaxal, zpmslbn, lsckdh\n" + 
			"mqreu (68)\n" + 
			"whrgexw (38)\n" + 
			"apyvj (52) -> zxorl, cxndnyb\n" + 
			"xagcn (59)\n" + 
			"bexgw (75)\n" + 
			"tewpgq (35)\n" + 
			"ctsij (83)\n" + 
			"crqmnp (180)\n" + 
			"vckvw (199) -> otrnol, jhbzqh\n" + 
			"bfmwj (61)\n" + 
			"fdtrij (62)\n" + 
			"goxwq (42) -> tyvrby, uqrnq, hyhkb, pukgrz, xivfthm, armoelp, zzxvkw\n" + 
			"fosawx (53)\n" + 
			"bzujyh (57) -> dbyss, attvd, hiunh, suvnzd\n" + 
			"wcbktof (96)\n" + 
			"vkmmhj (83)\n" + 
			"qglqj (361)\n" + 
			"fqeguqv (32)\n" + 
			"fwtbaig (25)\n" + 
			"xjitfcr (237) -> kukwwup, zlfmdxz\n" + 
			"lpqyotn (873) -> qdufhxj, zciggcz, atfeuv\n" + 
			"whckqd (65)\n" + 
			"fuxir (23)\n" + 
			"aajml (79)\n" + 
			"zrvyll (53)\n" + 
			"wvvuyku (83)\n" + 
			"twemenc (56)\n" + 
			"lpdfnmm (202)\n" + 
			"ckhdiiu (50)\n" + 
			"rvvvy (196) -> vpapi, qkqjab\n" + 
			"ibdxfru (51)\n" + 
			"adrfscr (87)\n" + 
			"gdoxoc (27)\n" + 
			"caqpd (16)\n" + 
			"dkpebdn (75) -> wngzj, qmkrh, hbummr, dquww\n" + 
			"frdghyw (15)\n" + 
			"vvfeefv (20) -> hbobj, zhhic\n" + 
			"dplsxrs (37)\n" + 
			"grvssv (210) -> aslxei, lnbgem, lipveyk, ffzmh\n" + 
			"revazr (89)\n" + 
			"lcldjzf (82)\n" + 
			"ispkg (41)\n" + 
			"jlbihbs (41)\n" + 
			"vlvwpx (92)\n" + 
			"gvojneq (81)\n" + 
			"uwlec (81)\n" + 
			"qkqjab (28)\n" + 
			"sedsofn (91)\n" + 
			"fqayp (336) -> zjefgu, rouhc, oqzhbrd\n" + 
			"dpoddhi (73)\n" + 
			"acddfq (175)\n" + 
			"fjhroj (93) -> tvjoswt, vlvwpx, ssqksg, pxggj\n" + 
			"ooztkre (92)\n" + 
			"mbtopdb (75) -> tqifa, mdupi\n" + 
			"cjgfncf (32)\n" + 
			"wjkajc (49)\n" + 
			"yikly (84) -> txelahj, qihaqhe\n" + 
			"rlafz (62)\n" + 
			"aouceoo (31)\n" + 
			"komzqjf (82)\n" + 
			"vlycnj (246)\n" + 
			"dwnqi (35)\n" + 
			"lgpewn (62)\n" + 
			"phshj (541) -> owdiwh, dustv, ljrzagq, ioobi\n" + 
			"peegv (29)\n" + 
			"uzbucf (49)\n" + 
			"rbgqim (7) -> gvywrbk, ptzhczg\n" + 
			"dixbd (26) -> bexgw, dyhpnf\n" + 
			"cijsvr (319)\n" + 
			"nlnmv (364)\n" + 
			"hkawy (62)\n" + 
			"tuylou (21)\n" + 
			"yvhgp (71)\n" + 
			"jztiyg (83)\n" + 
			"jrphokq (69) -> lxeit, szbbr, wgquj, rvvvy\n" + 
			"fqzmaf (75)\n" + 
			"qybci (176) -> hrcrej, eiwbgkd, hlpvxds, cuopz\n" + 
			"lwhlpt (27)\n" + 
			"sszivvi (9)\n" + 
			"anazl (21)\n" + 
			"lavsfcf (54)\n" + 
			"xaryc (91)\n" + 
			"mldaqph (21)\n" + 
			"eihvaj (222) -> dhibnu, gtzfc\n" + 
			"ftfgyt (86)\n" + 
			"vmsxkt (18)\n" + 
			"eykrgl (74)\n" + 
			"ipkim (209) -> fosawx, zrvyll, rguicjs\n" + 
			"qdcbmq (35) -> ignnpoe, acxxafw, vfsoty, vlgmhuv, igdqv, bdrgk\n" + 
			"hrcrej (15)\n" + 
			"tgvowq (65)\n" + 
			"lelkjof (81)\n" + 
			"myunfyp (1504) -> uzbucf, wwhwhlh\n" + 
			"fprcrwl (45) -> yjcjjwh, zzqucm, lopzzu, vcnhk, obtxdw, tlfhbr\n" + 
			"mfvzrxh (40) -> gizfx, vkxicq, lzckaxj\n" + 
			"atjml (18)\n" + 
			"vcikq (31)\n" + 
			"lzzakg (83)\n" + 
			"hxftzw (35)\n" + 
			"frbjm (77)\n" + 
			"pfluh (126) -> zlrcjv, gyibmx\n" + 
			"mkthfc (59)\n" + 
			"hkzcxkb (93)\n" + 
			"ywefb (63)\n" + 
			"iknffv (99)\n" + 
			"olugc (1992) -> nevpxb, oflnmu, vffjkzp\n" + 
			"sdamh (41)\n" + 
			"yfvrd (32)\n" + 
			"ktays (20)\n" + 
			"xdgvik (73)\n" + 
			"xcyvydj (18)\n" + 
			"eiqfjh (8516) -> atktbij, kaswwui, jyavyin\n" + 
			"itqaixc (28) -> gduajbd, vtomrxb, ajcedg\n" + 
			"muzit (92)\n" + 
			"tzpulfw (167) -> jykqgm, fzffa\n" + 
			"kalidnk (88)\n" + 
			"ushlvth (14) -> cdstxnu, nlnmv, cgwtrf\n" + 
			"hvzzdv (731) -> wsbkos, dbaczh, mdbdbii\n" + 
			"jlxfw (16) -> vlbvqd, lnezc\n" + 
			"owdiwh (15) -> mnlkn, xaryc\n" + 
			"sygqg (62)\n" + 
			"ekrfpes (257) -> dpoddhi, xjvzuax\n" + 
			"lixdk (57)\n" + 
			"xivfthm (65) -> ctsij, qtvfi\n" + 
			"lddpvs (57)\n" + 
			"lzpis (99)\n" + 
			"scxhcfn (35)\n" + 
			"axhgfu (142) -> hdptwar, eyttrr, vopvppg\n" + 
			"cxhafp (96)\n" + 
			"qtsfwnv (23)\n" + 
			"recld (128) -> fxtoawh, mewzj\n" + 
			"nrpfq (13)\n" + 
			"zovgme (66)\n" + 
			"sxyoft (92)\n" + 
			"lwkib (13)\n" + 
			"vcnhk (246) -> ohxkp, neqztkt\n" + 
			"hvqcxdx (20)\n" + 
			"oqyze (91)\n" + 
			"qrzjxx (75) -> znbuoor, sxukoj\n" + 
			"eebyqij (17)\n" + 
			"jpodlkc (95)\n" + 
			"nnnkqg (75)\n" + 
			"tvhttj (37)\n" + 
			"sintjhl (1076) -> bthky, ggapah, lpdfnmm, foooidm, kjtclbz, euhterr, fxlfsh\n" + 
			"obtxdw (244) -> ynnefdj, icdqutb\n" + 
			"dustv (87) -> gsqag, chrpwez\n" + 
			"nwnlk (221) -> tewpgq, scxhcfn\n" + 
			"itokb (14) -> yejlp, ysofby\n" + 
			"vmlot (5960) -> ruqwlqw, lpqyotn, lwhywr\n" + 
			"bhznh (226) -> iaekvbf, lmyoms\n" + 
			"ktrpmw (82) -> dkbunrw, mdhmr\n" + 
			"oizyf (97)\n" + 
			"jtzidh (39)\n" + 
			"vtkkzt (153) -> nrpfq, hjddl, qdqund, orvqjsu\n" + 
			"hxyfrr (73) -> dtnlyc, ghojvw\n" + 
			"xqdno (23) -> komzqjf, dwklefi\n" + 
			"mtwod (71)\n" + 
			"chttax (11)\n" + 
			"dwrnlgg (26)\n" + 
			"prnxzn (98)\n" + 
			"pgerst (361)\n" + 
			"qmvje (169) -> wkpka, eaxthh\n" + 
			"spyey (205)\n" + 
			"ixtywxj (34)\n" + 
			"tdaxn (17)\n" + 
			"ozafzy (405) -> lxdnpno, yctfou, oolchg, gylpg, putuzm, apunr, ylavfs\n" + 
			"soxeud (165) -> mgigu, ozynj\n" + 
			"clcdcgr (32)\n" + 
			"fgfjwf (62)\n" + 
			"xwzln (18)\n" + 
			"mdupi (99)\n" + 
			"hltkocy (1631) -> acddfq, rbusgi, kpbld, biigs\n" + 
			"qwmqr (30)\n" + 
			"vngxw (63)\n" + 
			"hqdse (95)\n" + 
			"anirf (83)\n" + 
			"ygmtia (26) -> kbmkrfb, soweon, ywlwppw, chcmfns, atngvo, qvasqa\n" + 
			"vyjyqm (72) -> tgzin, moxllkd, qwibwv, akrhumm\n" + 
			"dpily (299) -> moldik, fuiwxoi\n" + 
			"gcvvr (258) -> lnxnld, pthmd\n" + 
			"urcmmqc (52) -> qkxpxwf, jbnhonh, ftfgyt\n" + 
			"dhevr (63) -> gfofc, rtttief, whrgexw, azchhq\n" + 
			"ywdzbpm (98)\n" + 
			"trwmicb (151) -> anirf, nqzhi\n" + 
			"ahhobtu (68)\n" + 
			"rguicjs (53)\n" + 
			"iztqqg (4709) -> unhzsf, qbsauln, ddlrr\n" + 
			"vlanmkf (30)\n" + 
			"miyzuba (93)\n" + 
			"wwfsfm (35) -> rtpusqx, txuqarh, hltkocy, olugc, xfqbdst, trkar\n" + 
			"zloyhge (11)\n" + 
			"fxmyl (97)\n" + 
			"wnrch (34)\n" + 
			"hyvee (28)\n" + 
			"nevpxb (57) -> dxzrqa, txexr\n" + 
			"armoelp (81) -> fqzmaf, ztowevm\n" + 
			"nrukeq (261)\n" + 
			"hvvlhe (55)\n" + 
			"tqlxusp (83)\n" + 
			"oasngox (242) -> qdlqy, cfwjs, agvef, quayoh\n" + 
			"rxogya (20)\n" + 
			"vtomrxb (88)\n" + 
			"ekgzga (92)\n" + 
			"zjefgu (139) -> jfzpr, bataqig\n" + 
			"yskugrc (89)\n" + 
			"pbvhscz (112) -> pubnb, xbxsa, hicwq\n" + 
			"zidgf (93)\n" + 
			"bdbfwjx (54)\n" + 
			"lmyoms (90)\n" + 
			"nalot (38)\n" + 
			"zsgntfb (58)\n" + 
			"sjdcml (122) -> vbjrc, yptmfb, iqqovp\n" + 
			"qpqwnti (26)\n" + 
			"flsqqw (97) -> vtnddde, eihvaj, vzmxrqs, upnvuf, tziwukl, dzmst, rfzkn\n" + 
			"cgwtrf (79) -> hqdse, wtlxw, vfmuj\n" + 
			"ofwwstm (139) -> fgont, hvqcxdx\n" + 
			"rltnd (50)\n" + 
			"pqnhasu (63)\n" + 
			"enumule (73)\n" + 
			"dzmst (332) -> wdcers, cxcibc, ysphvx\n" + 
			"uegzzq (511) -> cccizey, yikly, vlycnj\n" + 
			"cwfhyvp (81)\n" + 
			"gvjwray (1402) -> obsfib, mftbcdj, kozpul, ahjzy\n" + 
			"gowpln (44) -> nnwvn, wgzdoiy, cidfsbr, psnhw\n" + 
			"gxxrr (1571) -> ouzekb, qbkxno, sintjhl, gxbbwl, gvjwray\n" + 
			"avsqfag (151) -> hzqvef, frdghyw\n" + 
			"qhqqpj (88) -> xpvhb, hvjak\n" + 
			"netob (153) -> lrndlu, pqkwc\n" + 
			"lvhelk (70)\n" + 
			"dbcevsv (56) -> hnuakg, xiiwbs, fprcrwl, jckhii, azowpjp\n" + 
			"igdqv (40) -> xvffp, fmtveft, apjgsl, tbnqbh, xdyhig\n" + 
			"ictjzx (18)\n" + 
			"iemfk (56)\n" + 
			"ssqksg (92)\n" + 
			"dkbunrw (71)\n" + 
			"knnre (11)\n" + 
			"cjamvug (84)\n" + 
			"mranee (81)\n" + 
			"djorufg (56)\n" + 
			"yfxlqu (2145) -> qhosh, nxphajd, slbbde\n" + 
			"giidwph (63) -> yztbahh, xphdneu\n" + 
			"zwrqsm (23)\n" + 
			"gsqcet (797) -> ptaorpg, oxyxxv, mbtopdb, eykvd, ksfylmr\n" + 
			"hbobj (80)\n" + 
			"tmonbh (41)\n" + 
			"rsxned (152) -> dixsodp, tvtbrol, viosv, ksqwp, gcvvr\n" + 
			"pqsufx (78) -> acfuxo, ngicn\n" + 
			"tzvye (84)\n" + 
			"putuzm (324)\n" + 
			"nixzhb (96)\n" + 
			"jpxxgq (18)\n" + 
			"rptcitf (97)\n" + 
			"kwnsatz (50)\n" + 
			"dkktvs (87)\n" + 
			"lklfzh (52)\n" + 
			"hixbkj (128) -> bxiroo, zverm\n" + 
			"eksvho (42)\n" + 
			"hmxve (70)\n" + 
			"faysq (69)\n" + 
			"kgifnwx (91)\n" + 
			"vtnddde (284) -> mldaqph, ziwqdh, rbbtkcf, bnuqzi\n" + 
			"ypvmcm (43)\n" + 
			"kumzgqd (5132) -> szphud, fqayp, gdmsj\n" + 
			"vlqjczn (48) -> dodxloj, lixdk\n" + 
			"qxmwtlw (50)\n" + 
			"iqqovp (80) -> fdtrij, scocac, fgfjwf, rlafz\n" + 
			"gwknw (12)\n" + 
			"clmdi (71)\n" + 
			"pwqlro (273) -> ywefb, olnmey\n" + 
			"uvkiha (8) -> xspgdc, cdgpv, djorufg\n" + 
			"jpfcy (80) -> gvrmz, yeano\n" + 
			"ylswd (23)\n" + 
			"ynunzdd (46) -> recld, pfnew, hofab, dsgvsd, pqsufx\n" + 
			"fnnaq (78)\n" + 
			"dbyss (57) -> ljmxejh, drpftnv, dwfoa\n" + 
			"leoalr (30)\n" + 
			"iatxfon (43)\n" + 
			"bnuqzi (21)\n" + 
			"idmwnxg (130) -> wzlfky, wnrch\n" + 
			"fyqrdda (82)\n" + 
			"jotjtdm (67)\n" + 
			"fvcnod (6)\n" + 
			"hepoax (467)\n" + 
			"boobyu (70)\n" + 
			"kjmhqa (35)\n" + 
			"eazmfh (32)\n" + 
			"vrupz (79)\n" + 
			"qvrlbp (54)\n" + 
			"vtdovx (5)\n" + 
			"dcxivs (26)\n" + 
			"upzpfu (78) -> tqlxusp, xuenhje\n" + 
			"pthmd (16)\n" + 
			"fikxbn (69) -> uznrazj, lddpvs\n" + 
			"extlmn (93)\n" + 
			"cxktgip (39)\n" + 
			"ftqnpo (23)\n" + 
			"gawxacm (67)\n" + 
			"gylpg (134) -> jpodlkc, zgfpv\n" + 
			"lxryza (20) -> avfse, mtinmla\n" + 
			"wdcpr (96)\n" + 
			"fmrcz (73)\n" + 
			"fxtoawh (5)\n" + 
			"lsdlhcv (66)\n" + 
			"ewdwpeh (64)\n" + 
			"ehqmjeh (48)\n" + 
			"lrymy (57)\n" + 
			"dvjdohw (875) -> pzmnutm, ydhtgb, xoexpuv, bbycv\n" + 
			"neqztkt (87)\n" + 
			"uyfpwa (78)\n" + 
			"wwzmxpr (73)\n" + 
			"fleler (140) -> cbxzq, ehqmjeh\n" + 
			"bxoyo (68)\n" + 
			"vvxbkb (111) -> gctir, gkeumf\n" + 
			"dhwffod (345) -> vmsxkt, lonjduc, ucodfmw\n" + 
			"ynoxdm (49)\n" + 
			"pxbps (29)\n" + 
			"hzqvef (15)\n" + 
			"dixsodp (136) -> ajllsx, pzmvwhg\n" + 
			"cbhxz (39)\n" + 
			"cqhzg (201) -> xtaoa, gcoqii\n" + 
			"ziwqdh (21)\n" + 
			"dsgkaw (319)\n" + 
			"vdrssx (47) -> myunfyp, jdsfjkf, rsxned, trnzjj, vhzfk, zvoxbcv\n" + 
			"euhterr (144) -> daznpq, qcmpct\n" + 
			"fchob (6)\n" + 
			"ijbcav (66)\n" + 
			"ozynu (84)\n" + 
			"ysyjko (204) -> yddky, cocpc\n" + 
			"xnuiprc (28)\n" + 
			"vlmtckh (82) -> cqmnmp, nranogq\n" + 
			"wxlrte (77) -> eavzu, urarg\n" + 
			"xmynm (13) -> nazazve, tmeconw, frbjm\n" + 
			"npzfhkv (207) -> oeddmx, iemfk\n" + 
			"ghojvw (98)\n" + 
			"kyfjsn (78)\n" + 
			"prqstpz (236) -> xamoxye, buhyicq, zloyhge\n" + 
			"pwfoj (35) -> oqyze, ysvmli, ftkcs, sedsofn\n" + 
			"npejje (42)\n" + 
			"aavyxnd (658) -> mjatmj, cblanil, tzpulfw\n" + 
			"wzcrtz (79)\n" + 
			"slgdz (16)\n" + 
			"eevawfz (26) -> tfrqfm, fegzjc\n" + 
			"qdqund (13)\n" + 
			"nctisdd (144) -> txtsesn, xuuboqp, cxktgip, kjkxg\n" + 
			"bpuuio (26)\n" + 
			"opcszf (55)\n" + 
			"szbbr (96) -> xkwuq, rqeosyf\n" + 
			"bxakn (28) -> zcxajvs, tkzjfkq\n" + 
			"kjjzo (74)\n" + 
			"yeano (60)\n" + 
			"cdgpv (56)\n" + 
			"hcason (73)\n" + 
			"ddaxciv (43)\n" + 
			"meecdj (21)\n" + 
			"yxtmb (54)\n" + 
			"bljxv (134) -> nbppbiy, npyxpyc\n" + 
			"fuusxze (84)\n" + 
			"pzozfud (32)\n" + 
			"wkpka (5)\n" + 
			"jykqgm (15)\n" + 
			"sjrdmho (44)\n" + 
			"ghicb (35)\n" + 
			"gcoqii (97)\n" + 
			"vlgck (24)\n" + 
			"qkxpxwf (86)\n" + 
			"wdmqsw (77)\n" + 
			"ttquuz (35)\n" + 
			"cccizey (146) -> rltnd, oooma\n" + 
			"gmsvr (315) -> klpjjvg, tptuy\n" + 
			"xfzgdj (62)\n" + 
			"rakwfhk (57)\n" + 
			"dryrgj (855) -> ljhtnf, nftuawm, trczg\n" + 
			"bebujq (66)\n" + 
			"wxscr (41)\n" + 
			"lqsjdu (195) -> niajghy, ispkg\n" + 
			"dhibnu (73)\n" + 
			"oaylpg (18)\n" + 
			"gctir (83)\n" + 
			"rdbxsg (94) -> xptngy, tqlbxbk, rysqk, wwjtzuf\n" + 
			"udaqlo (4921) -> csjtsvu, nwnlk, iljnbiz, lbjvtj\n" + 
			"iaekvbf (90)\n" + 
			"tesstj (84)\n" + 
			"rouhc (163) -> bebujq, ijbcav\n" + 
			"apunr (135) -> xoudko, eckrps, oyjliid\n" + 
			"dgcaijl (35)\n" + 
			"muaiipe (33) -> yskugrc, eughxu, revazr\n" + 
			"xluprc (1053) -> orxan, hvzzdv, hcphjb, sjdcml, ushlvth, xtnctiv, qfkkwc\n" + 
			"iirqqkc (14)\n" + 
			"rhprie (26)\n" + 
			"xcdpn (80)\n" + 
			"gduajbd (88)\n" + 
			"niajghy (41)\n" + 
			"tjhrdl (27)\n" + 
			"gtosn (90)\n" + 
			"vfsoty (974) -> vlqjczn, hixbkj, lxryza\n" + 
			"knmyjju (70)\n" + 
			"orxan (461) -> cvpimoj, ajafdiv, dhevr\n" + 
			"vhzfk (1053) -> fikxbn, wbpqtn, puqymd\n" + 
			"gbshnls (14)\n" + 
			"otkntx (37)\n" + 
			"suvnzd (126) -> nixzhb, wdcpr\n" + 
			"kpbld (23) -> dycdyx, gmlyf\n" + 
			"pzzlwt (13)\n" + 
			"iothmz (293) -> wekywpz, xwrkqj\n" + 
			"moldik (50)\n" + 
			"istfw (362) -> vvxbkb, hcltwvs, oexuzjy, wonidli, rbbrmfz, qnfxh\n" + 
			"nazazve (77)\n" + 
			"wqwckx (98) -> uvekre, eprwoep, qngdf, hkzcxkb\n" + 
			"fasqn (68)\n" + 
			"xoudko (63)\n" + 
			"lopdq (2026) -> goxwq, vptwcy, geexh, dvjdohw\n" + 
			"zowwvl (75) -> vfclsgp, unnwuw, lwzvpvx\n" + 
			"qmkrh (98)\n" + 
			"deizqho (35)\n" + 
			"suqkyqc (945) -> wcbktof, lrcfe, ajptvhc, cxhafp\n" + 
			"pebqce (59)\n" + 
			"locrn (3877) -> klgfmp, mbljzmm, ynunzdd\n" + 
			"xpvhb (55)\n" + 
			"fvoilc (87)\n" + 
			"jssuppj (61)\n" + 
			"trkar (1416) -> wxlrte, ryemzzp, ppltn, irduh, tqbqd\n" + 
			"aroct (38)\n" + 
			"zsrao (53) -> kzaee, xxlmsg, peegv, pxbps\n" + 
			"tvjoswt (92)\n" + 
			"ebacdan (71)\n" + 
			"acfuxo (30)\n" + 
			"rqeosyf (78)\n" + 
			"yjslt (15)\n" + 
			"robfda (7)\n" + 
			"qpjosh (90) -> gbaxyxf, vrupz, vahqnf, comkkpi\n" + 
			"rvvrxj (38)\n" + 
			"xhumie (166) -> bkyrrr, sbljlc\n" + 
			"ktlvla (99)\n" + 
			"jfzpr (78)\n" + 
			"gkgab (97)\n" + 
			"nsssqpp (68)\n" + 
			"yqhlag (31)\n" + 
			"yhrhiyx (94) -> zwngnd, lvhelk\n" + 
			"biigs (135) -> ktays, nmgdw\n" + 
			"jdbyghb (268) -> chrya, gwknw\n" + 
			"zverm (17)\n" + 
			"mwtcdof (23)\n" + 
			"acfeme (95)\n" + 
			"epeudep (49)\n" + 
			"yztbahh (39)\n" + 
			"ktrbnu (113) -> dsfeott, qiesity\n" + 
			"veojykb (70)\n" + 
			"dmzvtf (62)\n" + 
			"oflnmu (113)\n" + 
			"viosv (38) -> fuusxze, tesstj, gcqeyqn\n" + 
			"ukktl (49)\n" + 
			"bprczj (79)\n" + 
			"tgzin (86)\n" + 
			"wwpgr (83)\n" + 
			"hhoafz (90) -> sggqt, jxgoaqy\n" + 
			"pzmnutm (186) -> rxurj, xmzreq\n" + 
			"xlhtsn (28)\n" + 
			"lpxod (62)\n" + 
			"mhxta (38)\n" + 
			"vjhxwan (16)\n" + 
			"cqjena (23)\n" + 
			"acuce (5)\n" + 
			"btvpas (96)\n" + 
			"ccodif (31)\n" + 
			"ljhtnf (66) -> ylswd, hhsaqkq, qsxkcs, zwrqsm\n" + 
			"efozgh (34)\n" + 
			"nukmm (88)\n" + 
			"wekywpz (64)\n" + 
			"mgigu (11)\n" + 
			"asyku (72) -> ozynu, jsezaqz\n" + 
			"qiesity (74)\n" + 
			"wwumy (38)\n" + 
			"lopzzu (180) -> mypnwe, cdcpur, pjpcyya, ygqakoh\n" + 
			"lonjduc (18)\n" + 
			"fxzmev (110) -> qomla, jqiyk\n" + 
			"qekfk (80)\n" + 
			"tdyqxhp (116) -> mcwhhll, ooztkre\n" + 
			"gksdobg (23)\n" + 
			"ojhkvb (62)\n" + 
			"flwucgf (84)\n" + 
			"unpta (27)\n" + 
			"viaivun (118) -> ewdwpeh, okkry\n" + 
			"bysqw (56)\n" + 
			"xzwdltr (32)\n" + 
			"hcphjb (566) -> crqmnp, xhumie, vvfeefv\n" + 
			"gyibmx (95)\n" + 
			"olnmey (63)\n" + 
			"gmgscd (51)\n" + 
			"wlxgck (39)\n" + 
			"qbxat (9)\n" + 
			"amcrzt (329) -> ggrzmj, zovgme\n" + 
			"uodsyhs (88) -> cvyof, tsvpfin\n" + 
			"liedl (35)\n" + 
			"xlajoju (29)\n" + 
			"qbkxno (1953) -> ofwwstm, qmvje, uoscg\n" + 
			"sbljlc (7)\n" + 
			"mdbdbii (69) -> cdhed, fnsol\n" + 
			"qfhkrk (106) -> mshtzph, ujdaj, zokhh\n" + 
			"xdgfor (42) -> jotjtdm, lgooafx\n" + 
			"uznrazj (57)\n" + 
			"zjdnsr (20)\n" + 
			"epjrvvp (57)\n" + 
			"ayrzzu (6)\n" + 
			"lzckaxj (79)\n" + 
			"tfofygr (219) -> jmfvn, anazl\n" + 
			"zwapr (98)\n" + 
			"dlhnqsu (6)\n" + 
			"tbnqbh (41) -> cwfhyvp, gvojneq, kuyofu\n" + 
			"aslxei (25)\n" + 
			"snwwnem (30)\n" + 
			"qaxlov (74)\n" + 
			"ipzdtst (85)\n" + 
			"oxada (636) -> ipkim, qmghpah, gowpln\n" + 
			"yddky (53)\n" + 
			"cblanil (197)\n" + 
			"anujsv (71)\n" + 
			"wkvfo (177) -> muaiipe, nctisdd, tdyqxhp\n"
	};
}

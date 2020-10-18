package me.alexisevelyn.crewmate.enums.cosmetic;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Hat {
	NONE(0),
	ASTRONAUT(1),
	BASEBALL_CAP(2),
	BRAIN_SLUG(3),
	BUSH(4),
	CAPTAIN(5),
	DOUBLE_TOP_HAT(6),
	FLOWERPOT(7),
	GOGGLES(8),
	HARD_HAT(9),
	MILITARY(10),
	PAPER(11),
	PARTY_STRIPE(12),
	POLICE(13),
	STETHOSCOPE(14),
	TOP_HAT(15),
	TOWEL(16),
	USHANKA(17),
	VIKING(18),
	WALL_GUARD(19),
	SNOWMAN(20),
	ANTLERS(21),
	XMAS_LIGHTS(22),
	SANTA(23),
	XMAS_TREE(24),
	GIFT(25),
	CANDY_CANES(26),
	ELF_HAT(27),
	TWENTY_NINETEEN_PARTY(28),
	WHITE(29),
	CROWN(30),
	EYEBROWS(31),
	ANGEL_HALO(32),
	ELF_CAP(33),
	FLAT_CAP(34),
	PLUNGER(35),
	SNORKEL(36),
	STICKMIN(37),
	STRAW(38),
	SHERIFF(39),
	EYEBALL_LAMP(40),
	TOILET_PAPER(41),
	TOPPAT_CLAN_LEADER(42),
	BLACK_FEDORA(43),
	SKI_GOGGLES(44),
	LANDING_HEADSET(45),
	MIRA_HAZMAT(46),
	MEDICAL_MASK(47),
	MIRA_SECURITY_MASK(48),
	SAFARI(49),
	BANANA(50),
	BEANIE(51),
	BEAR_EARS(52),
	CHEESE(53),
	CHERRY(54),
	EGG(55),
	GREEN_FEDORA(56),
	FLAMINGO(57),
	FLOWER(58),
	KNIGHT_HELMET(59),
	PLANT(60),
	CAT_HEAD(61),
	BAT_WINGS(62),
	DEVIL_HORNS(63),
	MOHAWK(64),
	PUMPKIN(65),
	SPOOKY_PAPER_BAG(66),
	WITCH(67),
	WOLF_EARS(68),
	PIRATE(69),
	PLAGUE_DOCTOR(70),
	KNIFE(71),
	HOCKEY_MASK(72),
	MINER_GEAR(73),
	WINTER_GEAR(74),
	ARCHEOLOGIST(75),
	ANTENNA(76),
	BALLOON(77),
	BIRD_NEST(78),
	BANDANNA_BLACK(79),
	CAUTION_SIGN(80),
	CHEF(81),
	CCC(82),
	DO_RAG(83),
	DUM_STICKY_NOTE(84),
	FEZ(85),
	MILITARY_GENERAL(86),
	POMPADOUR(87),
	HUNTER(88),
	MILITARY_HELMET(89),
	CREWMATE(90),
	NINJA_MASK(91),
	RAM_HORNS(92),
	SNOW_CREWMATE(93);

	private final int hat;

	Hat(int hat) {
		this.hat = hat;
	}

	private static final java.util.Map<Integer, Hat> hatSearch = new HashMap<>();

	public int getHat() {
		return this.hat;
	}

	public static Hat getHat(int hatInteger) {
		return hatSearch.get(hatInteger);
	}

	public static String getHatName(Hat hat) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (hat) {
			case NONE:
				return translation.getString("hat_none");
			case ASTRONAUT:
				return translation.getString("hat_astronaut");
			case BASEBALL_CAP:
				return translation.getString("hat_baseball_cap");
			case BRAIN_SLUG:
				return translation.getString("hat_brain_slug");
			case BUSH:
				return translation.getString("hat_bush");
			case CAPTAIN:
				return translation.getString("hat_captain");
			case DOUBLE_TOP_HAT:
				return translation.getString("hat_double_top_hat");
			case FLOWERPOT:
				return translation.getString("hat_flowerpot");
			case GOGGLES:
				return translation.getString("hat_goggles");
			case HARD_HAT:
				return translation.getString("hat_hard_hat");
			case MILITARY:
				return translation.getString("hat_military");
			case PAPER:
				return translation.getString("hat_paper");
			case PARTY_STRIPE:
				return translation.getString("hat_party_stripe");
			case POLICE:
				return translation.getString("hat_police");
			case STETHOSCOPE:
				return translation.getString("hat_stethoscope");
			case TOP_HAT:
				return translation.getString("hat_top_hat");
			case TOWEL:
				return translation.getString("hat_towel");
			case USHANKA:
				return translation.getString("hat_ushanka");
			case VIKING:
				return translation.getString("hat_viking");
			case WALL_GUARD:
				return translation.getString("hat_wall_guard");
			case SNOWMAN:
				return translation.getString("hat_snowman");
			case ANTLERS:
				return translation.getString("hat_antlers");
			case XMAS_LIGHTS:
				return translation.getString("hat_xmas_lights");
			case SANTA:
				return translation.getString("hat_santa");
			case XMAS_TREE:
				return translation.getString("hat_xmas_tree");
			case GIFT:
				return translation.getString("hat_gift");
			case CANDY_CANES:
				return translation.getString("hat_candy_canes");
			case ELF_HAT:
				return translation.getString("hat_elf_hat");
			case TWENTY_NINETEEN_PARTY:
				return translation.getString("hat_2019_party");
			case WHITE:
				return translation.getString("hat_white");
			case CROWN:
				return translation.getString("hat_crown");
			case EYEBROWS:
				return translation.getString("hat_eyebrows");
			case ANGEL_HALO:
				return translation.getString("hat_angel_halo");
			case ELF_CAP:
				return translation.getString("hat_elf_cap");
			case FLAT_CAP:
				return translation.getString("hat_flat_cap");
			case PLUNGER:
				return translation.getString("hat_plunger");
			case SNORKEL:
				return translation.getString("hat_snorkel");
			case STICKMIN:
				return translation.getString("hat_stickmin");
			case STRAW:
				return translation.getString("hat_straw");
			case SHERIFF:
				return translation.getString("hat_sheriff");
			case EYEBALL_LAMP:
				return translation.getString("hat_eyeball_lamp");
			case TOILET_PAPER:
				return translation.getString("hat_toilet_paper");
			case TOPPAT_CLAN_LEADER:
				return translation.getString("hat_toppat_clan_leader");
			case BLACK_FEDORA:
				return translation.getString("hat_black_fedora");
			case SKI_GOGGLES:
				return translation.getString("hat_ski_goggles");
			case LANDING_HEADSET:
				return translation.getString("hat_landing_headset");
			case MIRA_HAZMAT:
				return translation.getString("hat_mira_hazmat");
			case MEDICAL_MASK:
				return translation.getString("hat_medical_mask");
			case MIRA_SECURITY_MASK:
				return translation.getString("hat_mira_security_mask");
			case SAFARI:
				return translation.getString("hat_safari");
			case BANANA:
				return translation.getString("hat_banana");
			case BEANIE:
				return translation.getString("hat_beanie");
			case BEAR_EARS:
				return translation.getString("hat_bear_ears");
			case CHEESE:
				return translation.getString("hat_cheese");
			case CHERRY:
				return translation.getString("hat_cherry");
			case EGG:
				return translation.getString("hat_egg");
			case GREEN_FEDORA:
				return translation.getString("hat_green_fedora");
			case FLAMINGO:
				return translation.getString("hat_flamingo");
			case FLOWER:
				return translation.getString("hat_flower");
			case KNIGHT_HELMET:
				return translation.getString("hat_knight_helmet");
			case PLANT:
				return translation.getString("hat_plant");
			case CAT_HEAD:
				return translation.getString("hat_cat_head");
			case BAT_WINGS:
				return translation.getString("hat_bat_wings");
			case DEVIL_HORNS:
				return translation.getString("hat_devil_horns");
			case MOHAWK:
				return translation.getString("hat_mohawk");
			case PUMPKIN:
				return translation.getString("hat_pumpkin");
			case SPOOKY_PAPER_BAG:
				return translation.getString("hat_spooky_paper_bag");
			case WITCH:
				return translation.getString("hat_witch");
			case WOLF_EARS:
				return translation.getString("hat_wolf_ears");
			case PIRATE:
				return translation.getString("hat_pirate");
			case PLAGUE_DOCTOR:
				return translation.getString("hat_plague_doctor");
			case KNIFE:
				return translation.getString("hat_knife");
			case HOCKEY_MASK:
				return translation.getString("hat_hockey_mask");
			case MINER_GEAR:
				return translation.getString("hat_miner_gear");
			case WINTER_GEAR:
				return translation.getString("hat_winter_gear");
			case ARCHEOLOGIST:
				return translation.getString("hat_archaeologist");
			case ANTENNA:
				return translation.getString("hat_antenna");
			case BALLOON:
				return translation.getString("hat_balloon");
			case BIRD_NEST:
				return translation.getString("hat_bird_nest");
			case BANDANNA_BLACK:
				return translation.getString("hat_bandanna_black");
			case CAUTION_SIGN:
				return translation.getString("hat_caution_sign");
			case CHEF:
				return translation.getString("hat_chef");
			case CCC:
				return translation.getString("hat_ccc");
			case DO_RAG:
				return translation.getString("hat_do_rag");
			case DUM_STICKY_NOTE:
				return translation.getString("hat_dum_sticky_note");
			case FEZ:
				return translation.getString("hat_fez");
			case MILITARY_GENERAL:
				return translation.getString("hat_military_general");
			case POMPADOUR:
				return translation.getString("hat_pompadour");
			case HUNTER:
				return translation.getString("hat_hunter");
			case MILITARY_HELMET:
				return translation.getString("hat_military_helmet");
			case CREWMATE:
				return translation.getString("hat_crewmate");
			case NINJA_MASK:
				return translation.getString("hat_ninja_mask");
			case RAM_HORNS:
				return translation.getString("hat_ram_horns");
			case SNOW_CREWMATE:
				return translation.getString("hat_snow_crewmate");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (Hat hatKey : Hat.values()) {
			hatSearch.put(hatKey.hat, hatKey);
		}
	}
}

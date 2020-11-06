package me.alexisevelyn.crewmate.enums.cosmetic;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Hat {
	NONE((byte) 0),
	ASTRONAUT((byte) 1),
	BASEBALL_CAP((byte) 2),
	BRAIN_SLUG((byte) 3),
	BUSH((byte) 4),
	CAPTAIN((byte) 5),
	DOUBLE_TOP_HAT((byte) 6),
	FLOWERPOT((byte) 7),
	GOGGLES((byte) 8),
	HARD_HAT((byte) 9),
	MILITARY((byte) 10),
	PAPER((byte) 11),
	PARTY_STRIPE((byte) 12),
	POLICE((byte) 13),
	STETHOSCOPE((byte) 14),
	TOP_HAT((byte) 15),
	TOWEL((byte) 16),
	USHANKA((byte) 17),
	VIKING((byte) 18),
	WALL_GUARD((byte) 19),
	SNOWMAN((byte) 20),
	ANTLERS((byte) 21),
	XMAS_LIGHTS((byte) 22),
	SANTA((byte) 23),
	XMAS_TREE((byte) 24),
	GIFT((byte) 25),
	CANDY_CANES((byte) 26),
	ELF_HAT((byte) 27),
	TWENTY_NINETEEN_PARTY((byte) 28),
	WHITE((byte) 29),
	CROWN((byte) 30),
	EYEBROWS((byte) 31),
	ANGEL_HALO((byte) 32),
	ELF_CAP((byte) 33),
	FLAT_CAP((byte) 34),
	PLUNGER((byte) 35),
	SNORKEL((byte) 36),
	STICKMIN((byte) 37),
	STRAW((byte) 38),
	SHERIFF((byte) 39),
	EYEBALL_LAMP((byte) 40),
	TOILET_PAPER((byte) 41),
	TOPPAT_CLAN_LEADER((byte) 42),
	BLACK_FEDORA((byte) 43),
	SKI_GOGGLES((byte) 44),
	LANDING_HEADSET((byte) 45),
	MIRA_HAZMAT((byte) 46),
	MEDICAL_MASK((byte) 47),
	MIRA_SECURITY_MASK((byte) 48),
	SAFARI((byte) 49),
	BANANA((byte) 50),
	BEANIE((byte) 51),
	BEAR_EARS((byte) 52),
	CHEESE((byte) 53),
	CHERRY((byte) 54),
	EGG((byte) 55),
	GREEN_FEDORA((byte) 56),
	FLAMINGO((byte) 57),
	FLOWER((byte) 58),
	KNIGHT_HELMET((byte) 59),
	PLANT((byte) 60),
	CAT_HEAD((byte) 61),
	BAT_WINGS((byte) 62),
	DEVIL_HORNS((byte) 63),
	MOHAWK((byte) 64),
	PUMPKIN((byte) 65),
	SPOOKY_PAPER_BAG((byte) 66),
	WITCH((byte) 67),
	WOLF_EARS((byte) 68),
	PIRATE((byte) 69),
	PLAGUE_DOCTOR((byte) 70),
	KNIFE((byte) 71),
	HOCKEY_MASK((byte) 72),
	MINER_GEAR((byte) 73),
	WINTER_GEAR((byte) 74),
	ARCHEOLOGIST((byte) 75),
	ANTENNA((byte) 76),
	BALLOON((byte) 77),
	BIRD_NEST((byte) 78),
	BANDANNA_BLACK((byte) 79),
	CAUTION_SIGN((byte) 80),
	CHEF((byte) 81),
	CCC((byte) 82),
	DO_RAG((byte) 83),
	DUM_STICKY_NOTE((byte) 84),
	FEZ((byte) 85),
	MILITARY_GENERAL((byte) 86),
	POMPADOUR((byte) 87),
	HUNTER((byte) 88),
	MILITARY_HELMET((byte) 89),
	CREWMATE((byte) 90),
	NINJA_MASK((byte) 91),
	RAM_HORNS((byte) 92),
	SNOW_CREWMATE((byte) 93);

	private final byte hat;
	private static final java.util.Map<Byte, Hat> hatSearch = new HashMap<>();

	Hat(byte hat) {
		this.hat = hat;
	}

	public byte getByte() {
		return this.hat;
	}

	@Nullable
	public static Hat getHat(byte hatByte) {
		return hatSearch.get(hatByte);
	}

	@NotNull
	@SuppressWarnings("PMD.ExcessiveMethodLength")
	public static String getHatName(@NotNull Hat hat) {
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

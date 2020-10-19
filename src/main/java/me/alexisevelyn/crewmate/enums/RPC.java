package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum RPC {
	ANIMATION((byte) 0),
	COMPLETE_TASK((byte) 1),
	SYNC_SETTINGS((byte) 2),
	SET_INFECTED((byte) 3),
	EXILED((byte) 4),
	CHECK_NAME((byte) 5),
	SET_NAME((byte) 6),
	CHECK_COLOR((byte) 7),
	SET_COLOR((byte) 8),
	SET_HAT((byte) 9),
	SET_SKIN((byte) 10),
	REPORT_DEAD_BODY((byte) 11),
	MURDER_PLAYER((byte) 12),
	SEND_CHAT((byte) 13),
	START_MEETING((byte) 14),
	SET_SCANNER((byte) 15),
	SEND_CHAT_NOTE((byte) 16),

	SET_PET((byte) 17),
	SET_START_COUNTER((byte) 18),
	ENTER_VENT((byte) 19),
	EXIT_VENT((byte) 20),
	SNAP_TO((byte) 21), // Teleport?
	CLOSE((byte) 22), // https://wiki.weewoo.net/wiki/Protocol#RPC_22_-_Close
	VOTING_COMPLETE((byte) 23),
	CAST_VOTE((byte) 24),
	CLEAR_VOTE((byte) 25),
	ADD_VOTE((byte) 26),
	CLOSE_DOOR((byte) 27),
	REPAIR_SYSTEM((byte) 28),
	SET_TASKS((byte) 29),
	UPDATE_GAME_DATA((byte) 30);

	private final int rpc;

	RPC(int rpc) {
		this.rpc = rpc;
	}

	private static final java.util.Map<Integer, RPC> rpcSearch = new HashMap<>();

	public int getRPC() {
		return this.rpc;
	}

	public static RPC getRPC(int rpcInteger) {
		return rpcSearch.get(rpcInteger);
	}

	public static String getRPCName(RPC rpc) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (rpc) {
			case ANIMATION:
				return translation.getString("rpc_animation");
			case COMPLETE_TASK:
				return translation.getString("rpc_complete_task");
			case SYNC_SETTINGS:
				return translation.getString("rpc_sync_settings");
			case SET_INFECTED:
				return translation.getString("rpc_set_infected");
			case EXILED:
				return translation.getString("rpc_exiled");
			case CHECK_NAME:
				return translation.getString("rpc_check_name");
			case SET_NAME:
				return translation.getString("rpc_set_name");
			case CHECK_COLOR:
				return translation.getString("rpc_check_color");
			case SET_COLOR:
				return translation.getString("rpc_set_color");
			case SET_HAT:
				return translation.getString("rpc_set_hat");
			case SET_SKIN:
				return translation.getString("rpc_set_skin");
			case REPORT_DEAD_BODY:
				return translation.getString("rpc_report_dead_body");
			case MURDER_PLAYER:
				return translation.getString("rpc_murder_player");
			case SEND_CHAT:
				return translation.getString("rpc_send_chat");
			case START_MEETING:
				return translation.getString("rpc_start_meeting");
			case SET_SCANNER:
				return translation.getString("rpc_set_scanner");
			case SEND_CHAT_NOTE:
				return translation.getString("rpc_send_chat_note");
			case SET_PET:
				return translation.getString("rpc_set_pet");
			case SET_START_COUNTER:
				return translation.getString("rpc_set_start_counter");
			case ENTER_VENT:
				return translation.getString("rpc_enter_vent");
			case EXIT_VENT:
				return translation.getString("rpc_exit_vent");
			case SNAP_TO:
				return translation.getString("rpc_snap_to");
			case CLOSE:
				return translation.getString("rpc_close");
			case VOTING_COMPLETE:
				return translation.getString("rpc_voting_complete");
			case CAST_VOTE:
				return translation.getString("rpc_cast_vote");
			case CLEAR_VOTE:
				return translation.getString("rpc_clear_vote");
			case ADD_VOTE:
				return translation.getString("rpc_add_vote");
			case CLOSE_DOOR:
				return translation.getString("rpc_close_door");
			case REPAIR_SYSTEM:
				return translation.getString("rpc_repair_system");
			case SET_TASKS:
				return translation.getString("rpc_set_tasks");
			case UPDATE_GAME_DATA:
				return translation.getString("rpc_update_game_data");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (RPC rpcKey : RPC.values()) {
			rpcSearch.put(rpcKey.rpc, rpcKey);
		}
	}
}

package me.alexisevelyn.crewmate;

import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.session.SessionContext;
import org.apache.sshd.common.session.SessionHeartbeatController;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.*;
import java.time.Duration;
import java.util.ArrayList;

public class Remote extends Thread {
	// https://github.com/apache/mina-sshd/blob/master/docs/server-setup.md

	@Override
	public void run() {
		SshServer sshd = SshServer.setUpDefaultServer();

		// For Non-Random Port
		sshd.setPort(22021);

		// For Heartbeat To Prevent Timeout
		sshd.setSessionHeartbeat(SessionHeartbeatController.HeartbeatType.IGNORE, Duration.ofSeconds(5));

		// For Host Key
		try {
			KeyPairProvider keyPairProvider = new TempKeyPairProvider();
			sshd.setKeyPairProvider(keyPairProvider);
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();

			return;
		}

		// For Password Based Authentication
		PasswordAuthenticator passwordAuthenticator = new TempPasswordAuthenticator();
		sshd.setPasswordAuthenticator(passwordAuthenticator);

		// For "Shell"
		ShellFactory shellFactory = new TempShellFactory();
		// ShellFactory shellFactory = new ProcessShellFactory("/bin/sh", "-i", "-l");
		sshd.setShellFactory(shellFactory);

		try {
			sshd.start();
		} catch (IOException exception) {
			LogHelper.printLine(Main.getTranslationBundle().getString("ssh_io_exception"));
		}
	}
}

class TempKeyPairProvider implements KeyPairProvider {
	private final KeyPairGenerator keyPairGenerator;
	private final KeyPair keyPair;

	public TempKeyPairProvider() throws NoSuchAlgorithmException {
		// Generate KeyPair
		keyPairGenerator = KeyPairGenerator.getInstance("RSA");

		// Apparently the seed's not enough to guarantee consistency for development
		keyPairGenerator.initialize(1024, new SecureRandom(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(1234).array()));
		keyPair = keyPairGenerator.generateKeyPair(); // new KeyPair(publicKey, privateKey);
	}

	@Override
	public Iterable<KeyPair> loadKeys(SessionContext session) throws IOException, GeneralSecurityException {
		ArrayList<KeyPair> keyPairList = new ArrayList<>();
		keyPairList.add(keyPair);

		return keyPairList;
	}
}

class TempPasswordAuthenticator implements PasswordAuthenticator {
	@Override
	public boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException, AsyncAuthException {
		return password.equals("testpassword");
	}

	@Override
	public boolean handleClientPasswordChangeRequest(ServerSession session, String username, String oldPassword, String newPassword) {
		throw new UnsupportedOperationException(Main.getTranslationBundle().getString("ssh_password_change_not_supported"));
	}
}

class TempShellFactory implements ShellFactory {
	@Override
	public Command createShell(ChannelSession channel) throws IOException {
		return new TempCommand();
	}
}

class TempCommand implements Command, Runnable {
	// https://github.com/apache/mina-sshd/blob/master/docs/commands.md

	private InputStream in;
	private OutputStream out;
	private OutputStream err;
	private ExitCallback exitCallback;

	@Override
	public void setInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	@Override
	public void setErrorStream(OutputStream err) {
		this.err = err;
	}

	@Override
	public void setExitCallback(ExitCallback callback) {
		this.exitCallback = callback;
	}

	@Override
	public void start(ChannelSession channel, Environment env) throws IOException {
		Thread self = new Thread(this);
		self.start();
	}

	@Override
	public void destroy(ChannelSession channel) throws Exception {
		// Not Implemented
	}

	@Override
	public void run() {
		try {
			out.write("Success!!!\n".getBytes());
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		this.exitCallback.onExit(0);
	}
}
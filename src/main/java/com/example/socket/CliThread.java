package com.example.socket;

import javafx.scene.layout.VBox;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class CliThread {

	static DataOutputStream objectOutputStream = null;
	DataInputStream objectInputStream = null;
	static String host = "";
	static int port = 9090;
	Socket socket = null;
	String MessageReceived = "";
	static String MessageToSend = "";
	boolean isConnected = false;
	boolean isFirst = false;

	static String client = "";

	CliThread() {
		try {
			socket = new Socket(host, port);
			System.out.println("Connecting....");

			objectOutputStream = new DataOutputStream(socket.getOutputStream());
			objectInputStream = new DataInputStream(socket.getInputStream());

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void closeConnection() {
		try {
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void sendMessage(String message) {
		try {
			objectOutputStream.writeUTF(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void movePiece(String from, String to) {
		sendMessage("MV:" + from + "-" + to);
	}

	private void processMessage(String message) {
		String type = message.split(":")[0];
		String messageContent = message.split(":")[1];

		switch (type) {
			case "MV":
				System.out.println("Movement type");
				movePiece("Circle1", "Circle2");
				break;
			case "TX":
				System.out.println("Text type");
				break;
			case "ST":
				isFirst = messageContent.equals("true");
				System.out.println(isFirst);
				System.out.println("Start type");
				break;
			case "CL":
				System.out.println("Close type");
				break;
			default:

		}
	}

	public void run() {
		isConnected = true;
		while (isConnected) {
			try {
				MessageReceived = objectInputStream.readUTF();

				System.out.println(MessageReceived);
				processMessage(MessageReceived);

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void main(String args[]) {
		host = args.length == 0 ? "localhost" : args[0];
		port = 9090;
		if (args.length == 2) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} else if (args.length == 1) {
			host = args[0];
		}
		new CliThread();
	}
}

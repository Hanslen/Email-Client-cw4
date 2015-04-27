import java.util.Date;
import java.util.Scanner;

public class View {
	private Controller controller;
	private static final String PROMPT = "?> ";
	private Scanner input;

	public View(Controller controller) {
		this.controller = controller;
		input = new Scanner(System.in);
	}

	public void getInput() {
		String response = "";
		String userInput = "";
		System.out.println("===== AE1ISO Mail Client ====");

		// special cases needed for quit and compose.
		while (!response.equals(CommandQuit.QUIT_MESSAGE)) {
			userInput = getUserInput();
			response = controller.processInput(userInput);
			System.out.println(response);
		}
	}

	public void compose(CommandCompose command) {
		String recipient;
		String from;
		String subject;
		String body;
		Date date = new Date();

		System.out.println("To: ");
		recipient = input.nextLine();
		System.out.println("From: ");
		from = input.nextLine();
		System.out.println("Subject: ");
		subject = input.nextLine();
		System.out.println("Body: ");
		body = input.nextLine();

		Message msg = new Message();
		msg.setRecipient(recipient);
		msg.setFrom(from);
		msg.setSubject(subject);
		msg.setBody(body);
		msg.setDate(date);
		command.setMessage(msg);
	}
	public void reply(CommandReply command, InterfaceClientModel model) {
		String recipient;
		String from;
		String subject;
		String body;
		Date date = new Date();

		System.out.println("To: "+model.returnholdMsg().getRecipient());
		recipient = model.returnholdMsg().getRecipient();
		System.out.println("From: "+model.returnholdMsg().getFrom());
		from = model.returnholdMsg().getFrom();
		System.out.println("Subject: "+"RE: "+model.returnholdMsg().getSubject());
		subject = "RE: "+model.returnholdMsg().getSubject();
		System.out.println("Body: ");
		body = input.nextLine();

		Message msg = new Message();
		msg.setRecipient(recipient);
		msg.setFrom(from);
		msg.setSubject(subject);
		msg.setBody(body);
		msg.setDate(date);
		command.setMessage(msg);
	}

	private String getUserInput() {
		System.out.print(PROMPT);
		return input.nextLine();
	}
}

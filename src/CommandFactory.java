public class CommandFactory {

	private static CommandFactory instance; // singleton

	private InterfaceClientModel model;
	private View view;
	
	private String previousCommand;

	private static final String COMMAND_CHANGE_FOLDER = "cf";
	private static final String COMMAND_LIST_DIRS = "listfolders";
	private static final String COMMAND_LIST_MESSAGES = "list";
	private static final String COMMAND_RENAME = "rename";
	private static final String COMMAND_SORT = "sort";
	private static final String COMMAND_MAKE_FOLDER = "mkf";
	private static final String COMMAND_MOVE = "move";
	private static final String COMMAND_COMPOSE = "compose";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_SEND_REC = "receive";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_QUIT = "quit";
	
	private static final String COMMAND_REPLY = "reply";
	private static final String COMMAND_FLAG = "flag";
	private static final String COMMAND_ALIAS = "alias";
	private static final String COMMAND_CR = "cr";

	public static CommandFactory getInstance() {
		if (instance == null) {
			instance = new CommandFactory();
		}
		return instance;
	}

	private CommandFactory() {
		// singleton.
	}

	public void setReferences(InterfaceClientModel model, View view) {
		this.model = model;
		this.view = view;
	}

	public AbstractCommand buildCommand(String command)
			throws BadCommandException {
		String[] commandInput = command.split(" ");
		commandInput[0] = model.getAlias().get(commandInput[0]);
		if(commandInput[0] == null){
			return new CommandBad(model, commandInput).setMessage("Error: Not a valid command: "
					+ command);
		}
		switch (commandInput[0]) {
		case COMMAND_CHANGE_FOLDER:
			return new CommandChangeFolder(model, commandInput);
		case COMMAND_LIST_DIRS:
			return new CommandListFolders(model, commandInput);
		case COMMAND_LIST_MESSAGES:
			return new CommandListMessages(model, commandInput);
		case COMMAND_RENAME:
			return new CommandRename(model, commandInput);
		case COMMAND_SORT:
			return new CommandSort(model, commandInput);
		case COMMAND_MAKE_FOLDER:
			return new CommandMakeFolder(model, commandInput);
		case COMMAND_MOVE:
			return new CommandMoveMessage(model, commandInput);
		case COMMAND_COMPOSE:
			return new CommandCompose(model, view, commandInput);
		case COMMAND_DELETE:
			return new CommandDelete(model, commandInput);
		case COMMAND_VIEW:
			return new CommandView(model, commandInput);
		case COMMAND_SEND_REC:
			return new CommandReceive(model, commandInput);
		case COMMAND_MARK:
			return new CommandMark(model, commandInput);
		case COMMAND_QUIT:
			return new CommandQuit(model, commandInput);
		case COMMAND_REPLY:
			return new CommandReply(model, view, commandInput);
		case COMMAND_FLAG:
			return new CommandFlag(model, commandInput);
		case COMMAND_ALIAS:
			return new CommandAlias(model, commandInput);
		case COMMAND_CR:
			return new CommandCr(model, commandInput);
		default:
			return new CommandBad(model, commandInput).setMessage("Error: Not a valid command: "
					+ command);
		}
	}

}

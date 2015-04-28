import java.util.ArrayList;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;

public class Controller {
	private InterfaceClientModel model;
	private CommandFactory commandFactory;
	private View view;
	private InterfaceConnector connector;
	
	ArrayList<String> stackCommand = new ArrayList<String>();
	ArrayList<String> undoCommand = new ArrayList<String>();
	int nowCommand = 0;
	int nowredocomamnd = 0;
	int redotimes = 1;
	
	public Controller() {
		view = new View(this);
		connector = StandardConnector.getInstance();
		model = new ClientModel(connector);
		commandFactory = CommandFactory.getInstance();
		commandFactory.setReferences(model, view);
	}

	public void begin() {
		view.getInput();
	}

	public String processInput(String theInput) {
		String response = "";
		//AbstractCommand command;
		int undo = 0;
		int redo = 0;
		String[] commandInput = theInput.split(" ");
		if(commandInput.length != 0){
			if(commandInput[0] != null){
				commandInput[0] = model.getAlias().get(commandInput[0]);
			}
		}
		if(commandInput[0] == null){
			commandInput[0] = "error";
		}
		try {
			if(commandInput[0].equals("undo")){
				nowCommand = stackCommand.size();
				if(nowCommand != 0 &&(nowCommand-1) >-1){
					response = commandFactory.buildCommand(stackCommand.get(nowCommand-1)).undo();
					undoCommand.add(stackCommand.get(nowCommand-1));
					stackCommand.remove(stackCommand.size()-1);
				}
				else{
					response = "Error: No command to undo";
				}
				undo = 1;
			}
			else if(commandInput[0].equals("redo")){
				nowredocomamnd = undoCommand.size();
				if(nowredocomamnd != 0 &&(nowredocomamnd-redotimes) >-1){
					response = commandFactory.buildCommand(undoCommand.get(nowredocomamnd-redotimes)).execute();
					stackCommand.remove(undoCommand.get(nowredocomamnd-redotimes));
					redotimes++;
				}
				else{
					response = "Error: No command to redo";
				}
				redo = 1;
			}
			if(undo == 0 && redo == 0){
				response = commandFactory.buildCommand(theInput).execute();
				undoCommand = new ArrayList<String>();
				redotimes = 1;
			}
			
			if(!response.substring(0, 5).equals("Error") && undo== 0&&redo== 0){
				stackCommand.add(theInput);
			}
			undo = 0;
			redo = 0;
			
		} catch (BadCommandException bce) {
			response = bce.getMessage();
		}
		return response;
	}
}

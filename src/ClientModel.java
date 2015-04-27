import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import emailConnector.InterfaceConnector;

public class ClientModel implements InterfaceClientModel {

	String inboxFolder = "inbox";
	String sentFolder = "sent";
	String greenFolder = "green";
	String redFolder = "red";
	String blueFolder = "blue";
	
	InterfaceMessage holdmsg;
	
	InterfaceConnector connector;
	HashMap<String, InterfaceFolder> folders = new HashMap<String, InterfaceFolder>();
	ArrayList<Integer> knownMessageIds;
	String previousFlag = "noflag";
	boolean previousIsRead = false;
	String[] previousrename = new String[2];
	String previouschangefolder;
	String previouscreatefolder;
	String[] previousmovemsgfolder = new String[2];
	String previousalias;
	int crcounter = 0;
	
	HashMap<String, String> alias = new HashMap<String, String>();
	String activeFolderName;
	ArrayList<InterfaceRules> rules = new ArrayList<InterfaceRules>();

	public ClientModel(InterfaceConnector connector) {
		this.connector = connector;
		folders.put(inboxFolder, new Folder());
		folders.put(sentFolder, new Folder());
		folders.put(greenFolder, new Folder());
		folders.put(redFolder, new Folder());
		folders.put(blueFolder, new Folder());
		activeFolderName = "inbox";
		knownMessageIds = new ArrayList<Integer>();
		alias.put("cf", "cf");
		alias.put("listfolders", "listfolders");
		alias.put("list", "list");
		alias.put("rename", "rename");
		alias.put("sort", "sort");
		alias.put("mkf", "mkf");
		alias.put("move", "move");
		alias.put("compose", "compose");
		alias.put("delete", "delete");
		alias.put("view", "view");
		alias.put("receive", "receive");
		alias.put("mark", "mark");
		alias.put("quit", "quit");
		alias.put("reply", "reply");
		alias.put("flag", "flag");
		alias.put("alias", "alias");
		alias.put("cr", "cr");
	}

	@Override
	public boolean changeActiveFolder(String name) {
		if (folders.containsKey(name)) {
			activeFolderName = name;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getActiveFolderName() {
		return activeFolderName;
	}

	@Override
	public boolean createFolder(String name) {
		if (!folders.containsKey(name)) {
			folders.put(name, new Folder());
			return true;
		}
		return false;

	}

	public Set<String> getFolderNames() {
		return folders.keySet();
	}

	@Override
	public boolean delete(int messageId) {
		InterfaceMessage temple;
		temple = folders.get(activeFolderName).getMessage(messageId);
		if (folders.get(activeFolderName).delete(messageId)) {
			connector.markMessageForDeleting(messageId);
			if(!temple.getFlag().equals("noflag")){
				if(activeFolderName.equals("inbox")){
					folders.get(folders.get(activeFolderName).getMessage(messageId).getFlag()).delete(messageId);
				}
				else{
					folders.get("inbox").delete(messageId);
				}
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean renameFolder(String oldName, String newName) {
		if(oldName.equals(inboxFolder) || oldName.equals(sentFolder)){
			return false;
		} else if (folders.containsKey(oldName) && !folders.containsKey(newName)) {
			folders.put(newName, folders.remove(oldName));
			activeFolderName = newName;
			return true;
		} else{
			return false;
		}
	}

	@Override
	public Collection<InterfaceMessage> getMessages() {
		return folders.get(activeFolderName).getMessages();
	}

	@Override
	public boolean mark(int messageId, boolean read) {
		InterfaceMessage msg = folders.get(activeFolderName).getMessage(
				messageId);
		if (msg != null) {
			msg.markRead(read);
			return true;
		}
		return false;
	}

	@Override
	public InterfaceMessage getMessage(int messageId) {
		return folders.get(activeFolderName).getMessage(messageId);
	}

	@Override
	public boolean move(int messageId, String folderName) {
		InterfaceMessage msg;
		InterfaceFolder folder = folders.get(folderName);
		msg = folders.get(activeFolderName).getMessage(messageId);

		if (folder == null || msg == null) {
			return false;
		}

		folder.addMessage(msg);
		folders.get(activeFolderName).delete(messageId);

		return true;
	}

	@Override
	public boolean checkForNewMessages() {
		int messageId;
		String[] messages;
		try {
			messages = connector.listMessages().split("\r\n");
			for (String message : messages) {
				if (!message.isEmpty()) {
					messageId = Integer.parseInt(message);
					if (!knownMessageIds.contains(messageId)) {
						knownMessageIds.add(messageId);
						folders.get(inboxFolder).addMessage(
								parseMessage(connector.retrMessage(messageId),
										messageId));
					}
				}
			}
			runtherules();
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	private InterfaceMessage parseMessage(String messageStr, int messageId) {
		Message message;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String[] messageString = messageStr.split("\r\n", 6);
		try {
			message = new Message();
			message.setId(messageId);
			message.setRecipient(messageString[0].split(":", 2)[1]);
			message.setFrom(messageString[1].split(":", 2)[1]);
			message.setDate(dateFormat.parse(messageString[2].split(":", 2)[1]));
			message.setSubject(messageString[3].split(":", 2)[1]);
			message.setBody(messageString[5]);
			message.setFlag("noflag");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return message;
	}

	@Override
	public boolean sendMessage(InterfaceMessage msg) {
		folders.get("sent").addMessage(msg);
		// message ID provided by the server once sent.
		String successResponse = connector.sendMessage(msg.toString());
		int providedId = Integer.parseInt(successResponse.split(" ")[1]);
		msg.setId(providedId);
		msg.setFlag("noflag");
		return true;
	}
	

	@Override
	public void sortByDate(boolean ascending) {
		folders.get(activeFolderName).sortByDate(false);
		
	}

	@Override
	public void sortBySubject(boolean ascending) {
		folders.get(activeFolderName).sortBySubject(true);
	}

	@Override
	public InterfaceFolder getFolder(String folderName) {
		return folders.get(folderName);
	}
	
	@Override
	public void setholdMsg(InterfaceMessage holdmsg){
		this.holdmsg = holdmsg;
	}
	
	@Override
	public InterfaceMessage returnholdMsg(){
		return this.holdmsg;
	}

	@Override
	public boolean flagMsg(int messageId, String flag) {
		// TODO Auto-generated method stub
		InterfaceMessage msg = folders.get(activeFolderName).getMessage(messageId);
		if (msg != null) {
			if(!msg.getFlag().equals("noflag")){
				getFolder(msg.getFlag()).delete(messageId);
			}
			msg.setFlag(flag);
			if(!flag.equals("noflag")){
				getFolder(flag).addMessage(msg);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean setAlias(String origin, String newAlias) {
		// TODO Auto-generated method stub
		if(alias.containsKey(origin)){			
			if(!alias.containsKey(newAlias)){
				alias.put(newAlias, origin);
				return true;
			}
		}
		return false;
	}

	@Override
	public HashMap<String, String> getAlias() {
		// TODO Auto-generated method stub
		return alias;
	}

	@Override
	public void setPreviousFlag(String flag) {
		// TODO Auto-generated method stub
		this.previousFlag = flag;
	}

	@Override
	public String getPreviousFlag() {
		// TODO Auto-generated method stub
		return this.previousFlag;
	}

	@Override
	public void setPeviousIsRead(boolean IsRead) {
		// TODO Auto-generated method stub
		previousIsRead = IsRead;
	}

	@Override
	public boolean getPreviousIsRead() {
		// TODO Auto-generated method stub
		return previousIsRead;
	}

	@Override
	public void setPreviousRename(String previousRename1, String previousRename2) {
		// TODO Auto-generated method stub
		this.previousrename[0] = previousRename1;
		this.previousrename[1] = previousRename2;
	}

	@Override
	public String[] getPreviousRename() {
		// TODO Auto-generated method stub
		return this.previousrename;
	}

	@Override
	public void setPreviouschangefolder(String previouschangefolder) {
		// TODO Auto-generated method stub
		this.previouschangefolder = previouschangefolder;
	}

	@Override
	public String getpreviouschangefolder() {
		// TODO Auto-generated method stub
		return this.previouschangefolder;
	}

	@Override
	public void setPreviouscreatefolder(String previouscreatefolder) {
		// TODO Auto-generated method stub
		this.previouscreatefolder = previouscreatefolder;
	}

	@Override
	public String getpreviouscreatefolder() {
		// TODO Auto-generated method stub
		return this.previouscreatefolder;
	}

	@Override
	public boolean deleteFolder(String deletefoldername) {
		// TODO Auto-generated method stub
		folders.remove(deletefoldername);
		return true;
	}

	@Override
	public void setpreviousmovemsgfolder(String previousmovemsgfolder1, String previousmovemsgfolder2) {
		// TODO Auto-generated method stub
		this.previousmovemsgfolder[0] = previousmovemsgfolder1;
		this.previousmovemsgfolder[1] = previousmovemsgfolder2;
	}

	@Override
	public String[] getpreviousmovemsgfolder() {
		// TODO Auto-generated method stub
		return this.previousmovemsgfolder;
	}

	@Override
	public void setpreviousalias(String previousalias) {
		// TODO Auto-generated method stub
		this.previousalias = previousalias;
	}

	@Override
	public String getpreviousalias() {
		// TODO Auto-generated method stub
		return this.previousalias;
	}

	@Override
	public boolean undoalias(String previousalias) {
		// TODO Auto-generated method stub
		alias.remove(previousalias);
		return true;
	}

	@Override
	public boolean cr(String destfolder, String searchtype,
			String searchkey) {
		// TODO Auto-generated method stub
		String[] helperforbody = null;
		crcounter = 0;
		String active = getActiveFolderName();
		ArrayList<Integer> crids = new ArrayList<Integer>();
		if (folders.containsKey(destfolder)) {
			Iterator<InterfaceMessage> itrmsg = folders.get("inbox").getMessages().iterator();
			changeActiveFolder("inbox");
			while(itrmsg.hasNext()){
				InterfaceMessage templemsg = itrmsg.next();
				String type = "";
				switch(searchtype){
					case "recipient": type = templemsg.getRecipient();break;
					case "sender":type = templemsg.getFrom();break;
					case "subject":type = templemsg.getSubject();break;
					case "body":type = templemsg.getBody(); helperforbody = type.split("\n");break;
					default:type = "";
				}
				if(type.matches("(.*)"+searchkey+"(.*)")&&!searchtype.equals("body")){
					crids.add(templemsg.getId());
					crcounter++;
				}
				if(searchtype.equals("body")){
					for(int i = 0; i < helperforbody.length; i++){
						if(helperforbody[i].matches("(.*)"+searchkey+"(.*)")){
							crids.add(templemsg.getId());
							crcounter++;
							break;
						}
					}
				}
				
			}
			Iterator<Integer> itrcounter = crids.iterator();
			while(itrcounter.hasNext()){
				move(itrcounter.next(), destfolder);
			}
			changeActiveFolder(active);
			return true;
		}
		return false;
	}

	@Override
	public int getcrcounter() {
		// TODO Auto-generated method stub
		return this.crcounter;
	}

	@Override
	public void runtherules() {
		// TODO Auto-generated method stub
		Iterator<InterfaceRules> itrrules = rules.iterator(); 
		while(itrrules.hasNext()){
			InterfaceRules nowrules = new InterfaceRules();
			nowrules=itrrules.next();
			cr(nowrules.getarg()[0],nowrules.getarg()[1],nowrules.getarg()[2]);
		}
	}

	@Override
	public void setrules(InterfaceRules onerule) {
		// TODO Auto-generated method stub
		this.rules.add(onerule);
	}


	
	
}

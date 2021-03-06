package model;

import java.util.ArrayList;

public class Tree extends GitObject implements hasName{
	private ArrayList<TreeEntry> treeEntriesList = new ArrayList<>();
	private String name;

	public Tree(String hash, byte[] b) {
		super(hash, "");
		this.type = GitObjectType.Tree;

		// Decode les données spécifiques aux trees
		StringBuffer buffer = new StringBuffer();
		StringBuffer lineBuffer = new StringBuffer(); // contient une seule
		// ligne de caractères
		boolean trad = false;
		int cpt = 0;
		for (byte by : b) {
			if (by == 0 && !trad) {
				buffer.append((char) 32);
				lineBuffer.append((char) 32);
				cpt = 20;
				trad = true;
			} else {
				if (trad) {
					String string = Integer.toHexString(by);
					if (string.length() > 2){
						string = string.substring(string.length() - 2);
					}
					else if(string.length() == 1){
						string = "0" + string;
					}
					buffer.append(string);
					lineBuffer.append(string);
					cpt--;
					if (cpt <= 0) {
						trad = false;
						buffer.append("\n");
						treeEntriesList.add(new TreeEntry(lineBuffer.toString()));
						lineBuffer.setLength(0);
					}
				} else {
					lineBuffer.append((char) by);
					buffer.append((char) by);
				}
			}
		}
		this.setRawData(buffer.toString());
	}

	public ArrayList<TreeEntry> getTreeEntriesList() {
		return treeEntriesList;
	}

	@Override
	public void setDataContent() {
		for (TreeEntry treeEntry : this.getTreeEntriesList()) {
			GitObject object =  this.getRepositoryData().getObjectByHash(treeEntry.getHash());
			if (object != null){
				((hasName)object).setName(treeEntry.getName());
				object.setParent(this);
			}
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}

package model;
import java.io.IOException;

import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;



public abstract class GitObject {
	GitObject parent;
	String hash;
	String rawData;
	protected GitObjectType type;

	protected GitObject(String hash, String rawData) {
		this.hash = hash;
		this.rawData = rawData;
	}

	public GitObject getParent() {
		return parent;
	}
	
	public void setParent(GitObject parent) {
		this.parent = parent;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public GitObjectType getType() {
		return this.type;
	}

	public static GitObject createGitObject(Repository repository, String hash) throws MissingObjectException, IOException {
		ObjectId id = repository.resolve(hash);
		ObjectLoader loader = repository.open(id);
		GitObject object = null;
		switch (loader.getType()) {
		case Constants.OBJ_COMMIT:
			object = new Commit(hash, new String(loader.getCachedBytes()));
			break;
		case Constants.OBJ_TREE:
			object = new Tree(hash, loader.getBytes());
			break;
		case Constants.OBJ_BLOB:
			object = new Blob(hash, new String(loader.getCachedBytes()));
			break;
		case Constants.OBJ_TAG:
			object = new Tag(hash, new String(loader.getCachedBytes()));
			break;
		}
		return object;
	}
}

package preprocessing;

import dataStructures.*;

public class TitleRemover {
	public void processes(Good toProcess) {
		String title = toProcess.getTitle();
		toProcess.setDescription(toProcess.getDescription().replaceAll(title, ""));
	}
}

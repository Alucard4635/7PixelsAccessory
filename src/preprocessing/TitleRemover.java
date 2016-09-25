package preprocessing;

import dataStructures.*;

public class TitleRemover implements GoodsProcesser {
	public void processes(Good toProcess) {
		String title = toProcess.getTitle();
		toProcess.setDescription(toProcess.getDescription().replaceAll(title, ""));
	}
}

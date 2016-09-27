package preprocessing;

import dataStructures.Good;

public class SpaceReplacer implements GoodsProcesser {

	@Override
	public void processes(Good toProcess) {
		toProcess.setTitle(toProcess.getTitle().replaceAll("(\\W)+", " "));
		toProcess.setDescription(toProcess.getDescription().replaceAll("(\\W)+", " "));

	}

}

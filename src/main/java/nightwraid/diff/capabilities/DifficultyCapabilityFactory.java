package nightwraid.diff.capabilities;

import java.util.concurrent.Callable;

public class DifficultyCapabilityFactory implements Callable<IDifficulty>{
	@Override
	public IDifficulty call() throws Exception {
		IDifficulty impl = new DifficultyCapability();
		return impl;
	}
}

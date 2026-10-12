package velrondevs.botania.coremod.spectrum;

import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

final class SpectrumJarLocator {
	private static final String MARKER = "spectrum.mixins.json";
	private static final String PREFIX = "de/dafuqs/spectrum/compat/";
	private static final byte[] REFERENCE = "vazkii/botania/".getBytes(StandardCharsets.US_ASCII);

	private SpectrumJarLocator() {}

	static Set<String> classNames() {
		for (Path candidate : candidates()) {
			Set<String> names = scan(candidate);
			if (!names.isEmpty()) {
				return names;
			}
		}
		return Collections.emptySet();
	}

	private static List<Path> candidates() {
		List<Path> paths = new ArrayList<>();
		try {
			Path mods = FMLPaths.MODSDIR.get();
			if (Files.isDirectory(mods)) {
				try (Stream<Path> files = Files.list(mods)) {
					files.filter(path -> path.getFileName().toString().endsWith(".jar")).forEach(paths::add);
				}
			}
		} catch (IOException | RuntimeException ignored) {
		}
		String legacy = System.getProperty("legacyClassPath.file");
		if (legacy != null) {
			try {
				for (String line : Files.readAllLines(Path.of(legacy))) {
					addEntry(paths, line);
				}
			} catch (IOException | RuntimeException ignored) {
			}
		}
		String classPath = System.getProperty("java.class.path");
		if (classPath != null) {
			for (String entry : classPath.split(java.io.File.pathSeparator)) {
				addEntry(paths, entry);
			}
		}
		return paths;
	}

	private static void addEntry(List<Path> paths, String entry) {
		String trimmed = entry.trim();
		if (trimmed.endsWith(".jar")) {
			try {
				paths.add(Path.of(trimmed));
			} catch (RuntimeException ignored) {
			}
		}
	}

	private static Set<String> scan(Path jar) {
		if (!Files.isRegularFile(jar)) {
			return Collections.emptySet();
		}
		try (JarFile file = new JarFile(jar.toFile())) {
			if (file.getEntry(MARKER) == null) {
				return Collections.emptySet();
			}
			Set<String> names = new LinkedHashSet<>();
			file.stream().forEach(entry -> {
				String name = entry.getName();
				if (name.startsWith(PREFIX) && name.endsWith(".class") && !name.endsWith("package-info.class") && !name.endsWith("module-info.class") && references(file, entry)) {
					names.add(name.substring(0, name.length() - ".class".length()).replace('/', '.'));
				}
			});
			return names;
		} catch (IOException | RuntimeException e) {
			return Collections.emptySet();
		}
	}

	private static boolean references(JarFile file, JarEntry entry) {
		try (InputStream in = file.getInputStream(entry)) {
			byte[] bytes = in.readAllBytes();
			for (int i = 0; i <= bytes.length - REFERENCE.length; i++) {
				int j = 0;
				while (j < REFERENCE.length && bytes[i + j] == REFERENCE[j]) {
					j++;
				}
				if (j == REFERENCE.length) {
					return true;
				}
			}
		} catch (IOException | RuntimeException ignored) {
		}
		return false;
	}
}

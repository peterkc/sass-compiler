package com.glide.scss;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.css.sac.CSSException;

import com.vaadin.sass.AbstractTestBase;
import com.vaadin.sass.internal.ScssStylesheet;
import com.yahoo.platform.yui.compressor.CssCompressor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author pete.chanthamynavong
 */
public class SassSpecTest extends AbstractTestBase {

	private static final Logger LOGGER = Logger.getLogger( SassSpecTest.class.getName() );

	private static final String INPUT_FILENAME = "input.scss";
	private static final String EXPECTED_FILENAME = "expected.compressed.css";

	@Test
	public void testSpecs() throws Exception {
		Path workingDir = Paths.get(System.getProperty("user.dir"));
		String specPattern = System.getProperty("sass.spec.pattern", "*");
		Path specPath = Paths.get(System.getProperty("sass.spec.dir", "sass-spec/spec"));

		final List<Path> files = new ArrayList<>();
		Files.walkFileTree(specPath, new PathSimpleFileVisitor(files, specPath, specPattern));

		int totalSpecs  = 0;
		int totalPassed = 0;

		for (Path path : files) {
			if (path.getFileName().endsWith(INPUT_FILENAME)) {
				totalSpecs++;

				if ( testPath(path, workingDir)  ) totalPassed++;
			}
		}

		LOGGER.info(String.format("directory: %s pattern: %s %n", specPath.toString(), specPattern));
		LOGGER.info(String.format("%s of %s passed, %s failed %n", totalPassed, totalSpecs, (totalSpecs - totalPassed)));
		assertThat(totalPassed, equalTo(totalSpecs));
	}

	public boolean testCompiler(Path scssPath, Path cssPath) throws Exception {
		ScssStylesheet sheet;

		try {
			sheet = getStyleSheet(scssPath);
			sheet.compile();
		} catch (Exception e) {
			LOGGER.info("==================================");
			LOGGER.info(scssPath.toString() + " (compiled) ");
			LOGGER.info(e.getMessage() + "\n\n");
			return false;
		}

		Writer parsedWriter = compress(sheet.printState());
		parsedScss = parsedWriter.toString();
		parsedScss = parsedScss.replaceAll(CR, "");
		parsedScss = parsedScss.trim();

		Writer comparisonWriter = compress(getFileContent(cssPath));
		comparisonCss = comparisonWriter.toString();
		comparisonCss = comparisonCss.replaceAll(CR, "");
		comparisonCss = comparisonCss.trim();

		boolean result = IOUtils.contentEquals(new StringReader(comparisonCss), new StringReader(parsedScss));

		if (!result) {
			LOGGER.info("==================================");
			LOGGER.info(scssPath.toString() + " (not equal)");
			LOGGER.info("Expected: \n" + comparisonCss);
			LOGGER.info("Actual: \n" + parsedScss);
			LOGGER.info("\n\n");
		}

		return result;
	}

	private boolean testPath(Path scssPath, Path workingDir) throws Exception {
		Path expectedPath = Paths.get(scssPath.getParent().toString(), EXPECTED_FILENAME);
		return testCompiler(scssPath, expectedPath);
	}

	private String getFileContent(Path path) throws IOException {
		return new String(Files.readAllBytes(path));
	}

	private ScssStylesheet getStyleSheet(Path filePath)
			throws URISyntaxException, CSSException, IOException {
		stylesheet = ScssStylesheet.get(filePath.toAbsolutePath().toString());
		return stylesheet;
	}

	private Writer compress(String css) throws IOException {
		Reader reader = new StringReader(css);
		CssCompressor compressor = new CssCompressor(reader);
		Writer writer = new StringWriter();
		compressor.compress(writer, -1);
		writer.close();
		return writer;
	}

	private static class PathSimpleFileVisitor extends SimpleFileVisitor<Path> {
		private final List<Path> fFiles;
		private final FileSystem fs = FileSystems.getDefault();

		private final Path fDirectory;
		private final PathMatcher fMatcher;

		PathSimpleFileVisitor(List<Path> files, Path directory, String pattern) {
			fFiles = files;
			fDirectory = directory;
			fMatcher = fs.getPathMatcher("glob:" + String.format("%s/%s", directory.toString(), pattern));
			LOGGER.info(String.format("%s/%s", directory.toString(), pattern));
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			if (fMatcher.matches(file) && !attrs.isDirectory()) {
				fFiles.add(file);
			}
			return FileVisitResult.CONTINUE;
		}
	}
}

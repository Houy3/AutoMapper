package os.modelmapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import os.memorandum.converters.ConcatConverter;
import os.memorandum.converters.RecursiveConverter;
import os.memorandum.converters.SimpleConverter;
import os.memorandum.core.AutoMapper;
import os.memorandum.core.MapConverter;
import os.memorandum.core.SimpleAutoMapper;

import static org.assertj.core.api.Assertions.assertThat;


class ModelMapperStarterApplicationTests {

	AutoMapper autoMapper = new SimpleAutoMapper();

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TestFrom {
		private String string;

		private String stringNull;

		private String stringOtherName;

		private String stringSameName;

		private int int1;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TestTo {
		private String string;

		private String stringNull;

		@MapConverter(from = {"stringOtherName"})
		private String stringNormalName;

		@MapConverter(converter = SimpleConverter.class)
		private String stringSameName;

		private String stringUnique;

		private int int1;
	}


	@Test
	void map_baseTransfer_success() {

		TestFrom testFrom = TestFrom.builder()
				.string("string")
				.stringOtherName("stringOtherName")
				.int1(1)
				.build();

		TestTo testTo = autoMapper.map(testFrom, TestTo.class);


        assertThat(testTo).isNotNull();
		assertThat(testTo.getString()).isEqualTo(testFrom.getString());
		assertThat(testTo.getStringNull()).isEqualTo(testFrom.getStringNull());
		assertThat(testTo.getStringNormalName()).isEqualTo(testFrom.getStringOtherName());
		assertThat(testTo.getInt1()).isEqualTo(testFrom.getInt1());

	}



	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TestFromConverter {
		private String stringSum1;

		private String stringSum2;

		private TestFromInner testInner;
	}

	@Data
	@AllArgsConstructor
	public static class TestFromInner {
		private String name;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TestToConverter {

		@MapConverter(from = {"stringSum1", "stringSum2"}, converter = ConcatConverter.class)
		private String stringSum;

		@MapConverter(converter = RecursiveConverter.class)
		private TestToInner testInner;
	}

	@Data
	@AllArgsConstructor
	public static class TestToInner {
		private String name;
	}


	@Test
	void map_converters_success() {

		TestFromConverter testFrom = TestFromConverter.builder()
				.stringSum1("stringSum1")
				.stringSum2("stringSum2")
				.testInner(new TestFromInner("name"))
				.build();

		TestToConverter testTo = autoMapper.map(testFrom, TestToConverter.class);


		assertThat(testTo).isNotNull();
		assertThat(testTo.getStringSum()).isEqualTo(testFrom.getStringSum1() + testFrom.getStringSum2());
		assertThat(testTo.getTestInner()).isNotNull();
		assertThat(testTo.getTestInner().getName()).isEqualTo(testFrom.getTestInner().getName());
	}


	@Test
	void mainTestVersion() {

//		List list = new ArrayList<>();
//
//		List list2 = list.stream().toList();


	}



}

package common.utils;

public final class Base64 {

	private static final int BASELENGTH = 128;
	private static final int LOOKUPLENGTH = 64;
	private static final int TWENTYFOURBITGROUP = 24;
	private static final int EIGHTBIT = 8;
	private static final int SIXTEENBIT = 16;
	private static final int FOURBYTE = 4;
	private static final int SIGN = -128;
	private static char PAD = '=';
	private static byte[] base64Alphabet = new byte[BASELENGTH];
	private static char[] lookUpBase64Alphabet = new char[LOOKUPLENGTH];
	final static String encodingChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	static {
		for (int i = 0; i < BASELENGTH; ++i) {
			base64Alphabet[i] = -1;
		}
		for (int i = 'Z'; i >= 'A'; i--) {
			base64Alphabet[i] = (byte) (i - 'A');
		}
		for (int i = 'z'; i >= 'a'; i--) {
			base64Alphabet[i] = (byte) (i - 'a' + 26);
		}

		for (int i = '9'; i >= '0'; i--) {
			base64Alphabet[i] = (byte) (i - '0' + 52);
		}

		base64Alphabet['+'] = 62;
		base64Alphabet['/'] = 63;

		for (int i = 0; i <= 25; i++) {
			lookUpBase64Alphabet[i] = (char) ('A' + i);
		}

		for (int i = 26, j = 0; i <= 51; i++, j++) {
			lookUpBase64Alphabet[i] = (char) ('a' + j);
		}

		for (int i = 52, j = 0; i <= 61; i++, j++) {
			lookUpBase64Alphabet[i] = (char) ('0' + j);
		}
		lookUpBase64Alphabet[62] = '+';
		lookUpBase64Alphabet[63] = '/';

	}

	private static boolean isWhiteSpace(char octect) {
		return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
	}

	private static boolean isPad(char octect) {
		return (octect == PAD);
	}

	private static boolean isData(char octect) {
		return (octect < BASELENGTH && base64Alphabet[octect] != -1);
	}

	/**
	 * Encodes hex octects into Base64
	 * 
	 * @param binaryData
	 *            Array containing binaryData
	 * @return Encoded Base64 array
	 */
	public static String encode(byte[] binaryData) {

		if (binaryData == null) {
			return null;
		}

		int lengthDataBits = binaryData.length * EIGHTBIT;
		if (lengthDataBits == 0) {
			return "";
		}

		int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
		int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
		int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1
				: numberTriplets;
		char encodedData[] = null;

		encodedData = new char[numberQuartet * 4];

		byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

		int encodedIndex = 0;
		int dataIndex = 0;

		for (int i = 0; i < numberTriplets; i++) {
			b1 = binaryData[dataIndex++];
			b2 = binaryData[dataIndex++];
			b3 = binaryData[dataIndex++];

			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);
			byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
					: (byte) ((b3) >> 6 ^ 0xfc);

			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
		}

		// form integral number of 6-bit groups
		if (fewerThan24bits == EIGHTBIT) {
			b1 = binaryData[dataIndex];
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
			encodedData[encodedIndex++] = PAD;
			encodedData[encodedIndex++] = PAD;
		} else if (fewerThan24bits == SIXTEENBIT) {
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);

			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
			encodedData[encodedIndex++] = PAD;
		}

		return new String(encodedData);
	}

	/**
	 * Decodes Base64 data into octects
	 * 
	 * @param encoded
	 *            string containing Base64 data
	 * @return Array containind decoded data.
	 */
	public static byte[] decode(String encoded) {

		if (encoded == null) {
			return null;
		}

		char[] base64Data = encoded.toCharArray();
		// remove white spaces
		int len = removeWhiteSpace(base64Data);

		if (len % FOURBYTE != 0) {
			return null;// should be divisible by four
		}

		int numberQuadruple = (len / FOURBYTE);

		if (numberQuadruple == 0) {
			return new byte[0];
		}

		byte decodedData[] = null;
		byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
		char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

		int i = 0;
		int encodedIndex = 0;
		int dataIndex = 0;
		decodedData = new byte[(numberQuadruple) * 3];

		for (; i < numberQuadruple - 1; i++) {

			if (!isData((d1 = base64Data[dataIndex++]))
					|| !isData((d2 = base64Data[dataIndex++]))
					|| !isData((d3 = base64Data[dataIndex++]))
					|| !isData((d4 = base64Data[dataIndex++]))) {
				return null;
			}// if found "no data" just return null

			b1 = base64Alphabet[d1];
			b2 = base64Alphabet[d2];
			b3 = base64Alphabet[d3];
			b4 = base64Alphabet[d4];

			decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
			decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
		}

		if (!isData((d1 = base64Data[dataIndex++]))
				|| !isData((d2 = base64Data[dataIndex++]))) {
			return null;// if found "no data" just return null
		}

		b1 = base64Alphabet[d1];
		b2 = base64Alphabet[d2];

		d3 = base64Data[dataIndex++];
		d4 = base64Data[dataIndex++];
		if (!isData((d3)) || !isData((d4))) {// Check if they are PAD characters
			if (isPad(d3) && isPad(d4)) {
				if ((b2 & 0xf) != 0)// last 4 bits should be zero
				{
					return null;
				}
				byte[] tmp = new byte[i * 3 + 1];
				System.arraycopy(decodedData, 0, tmp, 0, i * 3);
				tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				return tmp;
			} else if (!isPad(d3) && isPad(d4)) {
				b3 = base64Alphabet[d3];
				if ((b3 & 0x3) != 0)// last 2 bits should be zero
				{
					return null;
				}
				byte[] tmp = new byte[i * 3 + 2];
				System.arraycopy(decodedData, 0, tmp, 0, i * 3);
				tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
				tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				return tmp;
			} else {
				return null;
			}
		} else { // No PAD e.g 3cQl
			b3 = base64Alphabet[d3];
			b4 = base64Alphabet[d4];
			decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
			decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

		}

		return decodedData;
	}

	/**
	 * remove WhiteSpace from MIME containing encoded Base64 data.
	 * 
	 * @param data
	 *            the byte array of base64 data (with WS)
	 * @return the new length
	 */
	private static int removeWhiteSpace(char[] data) {
		if (data == null) {
			return 0;
		}

		// count characters that's not whitespace
		int newSize = 0;
		int len = data.length;
		for (int i = 0; i < len; i++) {
			if (!isWhiteSpace(data[i])) {
				data[newSize++] = data[i];
			}
		}
		return newSize;
	}

	/**
	 * Returns the base 64 encoded equivalent of a supplied string.
	 * 
	 * @param source
	 *            the string to encode
	 */
	public static String EnCode(String source) {
		char[] sourceBytes = getPaddedBytes(source);
		int numGroups = (sourceBytes.length + 2) / 3;
		char[] targetBytes = new char[4];
		char[] target = new char[4 * numGroups];

		for (int group = 0; group < numGroups; group++) {
			convert3To4(sourceBytes, group * 3, targetBytes);
			for (int i = 0; i < targetBytes.length; i++) {
				target[i + 4 * group] = encodingChar.charAt(targetBytes[i]);
			}
		}

		int numPadBytes = sourceBytes.length - source.length();

		for (int i = target.length - numPadBytes; i < target.length; i++)
			target[i] = '=';
		return new String(target);
	}

	private static char[] getPaddedBytes(String source) {
		char[] converted = source.toCharArray();
		int requiredLength = 3 * ((converted.length + 2) / 3);
		char[] result = new char[requiredLength];
		System.arraycopy(converted, 0, result, 0, converted.length);
		return result;
	}

	private static void convert3To4(char[] source, int sourceIndex,
			char[] target) {
		target[0] = (char) (source[sourceIndex] >>> 2);
		target[1] = (char) (((source[sourceIndex] & 0x03) << 4) | (source[sourceIndex + 1] >>> 4));
		target[2] = (char) (((source[sourceIndex + 1] & 0x0f) << 2) | (source[sourceIndex + 2] >>> 6));
		target[3] = (char) (source[sourceIndex + 2] & 0x3f);
	}

	/**
	 * Returns the plaintext equivalent of a base 64-encoded string.
	 * 
	 * @param source
	 *            a base 64 string (which must have a multiple of 4 characters)
	 */
	public static String DeCode(String source) {
		if (source.length() % 4 != 0)
			throw new RuntimeException(
					"valid Base64 codes have a multiple of 4 characters");
		int numGroups = source.length() / 4;
		int numExtraBytes = source.endsWith("==") ? 2
				: (source.endsWith("=") ? 1 : 0);
		byte[] targetBytes = new byte[3 * numGroups];
		byte[] sourceBytes = new byte[4];
		for (int group = 0; group < numGroups; group++) {
			for (int i = 0; i < sourceBytes.length; i++) {
				sourceBytes[i] = (byte) Math.max(0,
						encodingChar.indexOf(source.charAt(4 * group + i)));
			}
			convert4To3(sourceBytes, targetBytes, group * 3);
		}
		return new String(targetBytes, 0, targetBytes.length - numExtraBytes);
	}

	private static void convert4To3(byte[] source, byte[] target,
			int targetIndex) {
		target[targetIndex] = (byte) ((source[0] << 2) | (source[1] >>> 4));
		target[targetIndex + 1] = (byte) (((source[1] & 0x0f) << 4) | (source[2] >>> 2));
		target[targetIndex + 2] = (byte) (((source[2] & 0x03) << 6) | (source[3]));
	}

}

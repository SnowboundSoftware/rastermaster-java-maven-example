/*
 * Copyright (C) 2021 by Snowbound Software Corporation. All rights reserved.
 * This example is provided as-is and without warranty.
 * Please visit our main support page at snowbound.com/support or contact us at +1-617-607-2010.
 */

package com.snowbound.re.maven_examples.conversion;

import java.io.*;
import com.snowbound.licensemanager.LicenseManager;
import Snow.*;


public class Main
{
	/**
	 * Snowbound RasterMaster document conversion example using Maven
	 *
	 * @param args Program expects args[0] to be the path to an input file
	 *                and args[1] to be the path to a SnowboundLicense.jar
	 */
	public static void main(String[] args)
	{
		String inputFile;
		String licenseFile;

		// Change this to a licensed format if desired
		int outputFormat = Defines.PNG;

		// Parse arguments
		if(args.length == 2)
		{
			inputFile = args[0];
			licenseFile = args[1];

			// Check if the input file exists
			if(!new File(inputFile).exists())
			{
				System.out.println("Input file specified does NOT exist");
				return;
			}

			// Check if the license file exists
			if(!new File(licenseFile).exists())
			{
				System.out.println("License file specified does NOT exist");
				return;
			}
		}
		else
		{
			// Called if no arguments are presented
			System.out.println("Requires input file path and license JAR file path arguments");
			return;
		}

		// Convert a document
		convertDocument(inputFile, licenseFile, outputFormat);
	}

	/**
	 * Convert a document
	 * @param inputFile File to convert
	 * @param licenseFile Path to a SnowboundLicense.jar
	 * @param outputFormat Format to output as
	 */
	static void convertDocument(String inputFile, String licenseFile, int outputFormat)
	{
		// create the Snowbound image object
		Snowbnd snowImage = new Snowbnd();
		int status;

		// Load license
		try
		{
			status = snowImage.IMGLOW_set_license_jar(new FileInputStream(licenseFile));

			if (0 > status)
			{
				System.out.println(
						"ERROR: " + status + " [" + ErrorCodes.getErrorMessage(status) + "] loading license JAR"
				);
			}

			System.out.println("Licensed to: " + LicenseManager.customerName());
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getMessage());
			return;
		}

		// create the input and output file extensions
		int fileType = snowImage.IMGLOW_get_filetype(inputFile);

		// always check for errors
		if (0 > fileType) {
			System.out.println("Please edit the 'inputFile' variable to specify a good filename. ["
					+ ErrorCodes.getErrorMessage(fileType) + "] processing input file " + inputFile);
			return;
		}

		FormatHash formatHash = FormatHash.getInstance();
		Format inputExt = formatHash.getFormat(fileType);
		Format outputExt = formatHash.getFormat(outputFormat);

		// print the file type of the input & output files
		System.out.println("converting " + inputExt.getFormatName() + " to " + outputExt.getFormatName() + "...");

		// print the total number of pages in the input document
		int totalPages = snowImage.IMGLOW_get_pages(inputFile);
		System.out.println("total number of pages = " + totalPages);

		// Do conversion
		for (int page = 0; page < totalPages; page++)
		{
			snowImage.threshold = 128;

			if (outputFormat == Defines.SVG)
			{
				try {
					RandomAccessFile inFileRAF = new RandomAccessFile(inputFile, "r");
					int[] length = new int[2];
					int[] error = new int[2];
					int size = (int) inFileRAF.length();
					byte[] buff = new byte[size];
					inFileRAF.readFully(buff, 0, size);

					DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buff));
					byte[] vectorSVG = snowImage.IMG_vector_to_svg(dis, length, error, page);

					try
					{
						RandomAccessFile outFileRAF = new RandomAccessFile(
								inputFile + page + "_out." + outputExt.getExtension(),
								"rw"
						);
						outFileRAF.write(vectorSVG, 0, length[0]);
					}
					catch (IOException e)
					{
						System.out.println(e.getMessage());
						return;
					}
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
					return;
				}

				if (0 > status)
				{
					System.out.println(
							"ERROR: " + status + " [" + ErrorCodes.getErrorMessage(status) + "] on p. {" + page + "}"
					);
					return;
				}
			}
			else if (outputFormat == Defines.PDF)
			{
				int[] length = new int[2];
				int[] error = new int[2];
				byte[] extractedText = snowImage.IMGLOW_extract_text(inputFile, length, error, page);
				status = snowImage.IMG_save_document(
						(inputFile + page + "_out." + outputExt.getExtension()),
						extractedText,
						Defines.PDF
				);

				if (0 > status) {
					System.out.println(
							"ERROR: " + status + " [" + ErrorCodes.getErrorMessage(status) + "] on p. {" + page + "}");
					return;
				}
			}
			else
			{
				status = snowImage.IMG_decompress_bitmap(inputFile, page);
				System.out.println("decompression status = " + status);

				// print the error code details if decompression fails
				if (0 > status) {
					System.out.println(
							"ERROR: " + status + " [" + ErrorCodes.getErrorMessage(status) + "] on p. {" + page + "}"
					);
					return;
				}

				// save the current page and print the resulting status code
				status = snowImage.IMG_save_bitmap(
						inputFile + page + "_out." + outputExt.getExtension(),
						outputFormat
				);
			}

			System.out.println("save status = " + status);

			// print the error code details if save fails
			if (0 > status) {
				System.out.println(
						"ERROR on save: " + status + " [" + ErrorCodes.getErrorMessage(status) + "] on p. {" + page + "}"
				);
				return;
			}

			System.out.println("Done p. " + page);
		}
		System.out.println("conversion completed!");
	}
}
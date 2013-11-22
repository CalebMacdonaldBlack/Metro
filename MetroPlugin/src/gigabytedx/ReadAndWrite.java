package gigabytedx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadAndWrite {

	public static List<Object> modules = new ArrayList<Object>();

	public static void writeFile(String objectName, Object object, File dataFolder) {

		try {

			// if region data is saved read from it otherwise create a new one..
			read(objectName, dataFolder);

			// copy data from file to a new object

		} catch (FileNotFoundException e) {

			// if file was not found, attempt to create a new one
			Main.sendDebugInfo("Can't find data file. Attepting to create a new one");

			try {

				// attempt to write regions object to file
				save(objectName, object, dataFolder);
				Main.sendDebugInfo("New data file created successfully!");

			} catch (FileNotFoundException e1) {

				// if FileNotFoundException send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the data file : FileNotFoundException");
				e1.printStackTrace();
			}
		}

	}

	public static void save(String fileName, Object object, File dataFolder) throws FileNotFoundException {

		// prepare to write to file
		File tempName = new File(dataFolder, fileName);
		try {
			tempName.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		FileOutputStream fout = new FileOutputStream(tempName);
		ObjectOutputStream oos;

		try {

			// attempt to write to file
			oos = new ObjectOutputStream(fout);

			try {

				// attempt to save regions object to file
				oos.writeObject(object);
				Main.sendDebugInfo(" data saved successfully!");

				try {

					// attempt to close file output stream
					fout.close();

				} catch (IOException e) {

					// if IOexception send error msg to console
					Main.sendSevereInfo("A problem occurred attempting to read from the data file : IOException");
					e.printStackTrace();
				}
			} catch (IOException e) {

				// if IOexception send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the data file : IOException");
				e.printStackTrace();
			}

		} catch (IOException e1) {

			// if IOexception send error msg to console
			Main.sendSevereInfo("A problem occurred attempting to read from the data file : IOException");
			e1.printStackTrace();
		}

	}

	public static Object read(String fileName, File dataFolder) throws FileNotFoundException {

		// prepare to read file
		File tempName = new File(dataFolder, fileName);
		FileInputStream fin = new FileInputStream(tempName);
		ObjectInputStream ois;
		Object temp = null;

		try {
			// attempt to read file
			ois = new ObjectInputStream(fin);
			try {
				// attempt to save data from file to object.

				temp = ois.readObject();
				Main.sendDebugInfo(" data accessed successfully!");

			} catch (ClassNotFoundException e) {

				// if class wasn't found send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the data file : ClassNotFoundException");
				e.printStackTrace();

			} catch (IOException e) {

				// if IOexception send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the data file : IOException");
				e.printStackTrace();
			}
		} catch (IOException e) {

			// if IOexception send error msg to console
			Main.sendSevereInfo("A problem occurred attempting to read from the data file : IOException");
			e.printStackTrace();
		}
		try {

			// attempt to close input stream
			fin.close();
			return (Object) temp;

		} catch (IOException e) {

			// if IOexception send error msg to console
			Main.sendSevereInfo("A problem occurred attempting to read from the data file : IOException");
			e.printStackTrace();

		}
		return null;
	}

}

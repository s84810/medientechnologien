import java.io.*;
import java.nio.charset.StandardCharsets;

public class wave_io {
    public static void main(String[] args) {
        int samples = 0;
        int validBits = 0;
        long sampleRate = 0;
        long numFrames = 0;
        int numChannels = 0;

        String inFilename = null;
        String outFilename = null;

        if (args.length < 1) {
            try {
                throw new WavFileException("At least one filename specified  (" + args.length + ")");
            } catch (WavFileException e1) {
                e1.printStackTrace();
            }
        }

        // Samples in dem Array readWavFile.sound

        inFilename = args[0];

        // Implementierung bei einem Eingabeparameter

        WavFile readWavFile = null;
        try {
            readWavFile = WavFile.read_wav(inFilename);

            // headerangaben
            numFrames = readWavFile.getNumFrames();
            numChannels = readWavFile.getNumChannels();
            samples = (int) numFrames * numChannels;
            validBits = readWavFile.getValidBits();
            sampleRate = readWavFile.getSampleRate();
            // Textdatei für samples laden
            String projectDir = System.getProperty("user.dir");
            Writer writer = new OutputStreamWriter(new FileOutputStream(projectDir + "/output/samples.txt"), StandardCharsets.US_ASCII);
            // 2a Samples schreiben
            for (int i = 0; i < samples; i++) {
                writer.write(String.valueOf(readWavFile.sound[i]) + "\n");
            }
            writer.close();

            if (args.length == 1)
                System.exit(0);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (WavFileException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        // Implementierung bei Ein-und Ausgabeparameter

        outFilename = args[1];
        try {

//			// 2e Downsampling
//			for (int i=0; i < samples / 2; i++) {
//				readWavFile.sound[i] = readWavFile.sound[i * 2];
//			}
//			sampleRate = readWavFile.getSampleRate() / 2;
//			numFrames = readWavFile.getNumFrames() / 2;

//			// 3b Bitreduzierung
//			int reduced_bits = 10;
//			for (int i=0; i < samples;i++) {
//				readWavFile.sound[i] /= Math.pow(2, reduced_bits);
//				readWavFile.sound[i] *= Math.pow(2, reduced_bits);
//			}

            // 3e Bitreduzierung Differenz
            int reduced_bits = 1;
            double difference;
			// Textdatei für samples laden
			String projectDir = System.getProperty("user.dir");
			Writer writer = new OutputStreamWriter(new FileOutputStream(projectDir + "/output/difference.txt"), StandardCharsets.US_ASCII);
            for (int i = 0; i < samples; i++) {
                double original = readWavFile.sound[i];
                readWavFile.sound[i] /= Math.pow(2, reduced_bits);
                readWavFile.sound[i] *= Math.pow(2, reduced_bits);
                difference = (original - readWavFile.sound[i]) * Math.pow(2, 16 - reduced_bits - 1);
                writer.write(difference + "\n");
            }
			writer.close();

            WavFile.write_wav(outFilename, numChannels, numFrames, validBits, sampleRate, readWavFile.sound);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}

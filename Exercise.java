/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package exercise;
import java.io.*;
import static java.lang.Math.sqrt;
import java.util.*;

/**
*
* @author suran
*/
public class Exercise {
/**
* @param args the command line arguments
     * @throws java.io.IOException
*/
    
    public static void main(String[] args) throws IOException 
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the directory from where the files needs to be fetched: ");
        String path = sc.nextLine();
        File directory = new File(path);
        System.out.println();
        Similar s = new Similar();
        File[] files = directory.listFiles();
        for(int i =0;i<files.length;i++)
        {
            File f1 = files[i];
            if((f1.getName().contains(".docx")) || f1.getName().contains(".pdf"))
                continue;
            else
            {
                for(int j=i+1;j<files.length;j++)
                {
                    File f2 = files[j];
                    if((f2.getName().contains(".docx")) || f2.getName().contains(".pdf"))
                        continue;
                    else
                        s.check_similarity(f1, f2);
                }
            }
        }
        
    }
}




class Similar
{
    public double check_similarity(File f1, File f2) throws FileNotFoundException, IOException
    {
        System.out.println("The files names being checked for plagiarism: ");
        System.out.println(f1.getName());
        System.out.println(f2.getName());
        
        double similarity = 0;
        FileInputStream fin1=new FileInputStream(f1);
        FileInputStream fin2=new FileInputStream(f2);
        
        // Reading the entire data in file all at once
        byte b1[]=new byte[(int)f1.length()];
        byte b2[]=new byte[(int)f2.length()];
        fin1.read(b1);
        fin2.read(b2);
        String full1=new String(b1);
        String full2=new String(b2);
        String[] both = {full1,full2};
        
        // Cleaning of data
        for(int i=0;i<both.length;i++)
        {
            both[i] = both[i].replaceAll("[^A-Za-z0-9_-]", " ");
            both[i] = both[i].replaceAll("  ", " ");
        }
        String p=both[0]+" "+both[1];
        String edited[][]={both[0].split(" "),both[1].split(" ")};
        
        // Hash set for all unique words in the set
        Set<String> unique_words =new HashSet<>(Arrays.asList(p.split(" ")));
        System.out.println("File 1 word count:"+edited[0].length);
        System.out.println("File 2 word count:"+edited[1].length);
        System.out.println("Set elements: "+unique_words.size());
        double[][] frequency = new double[unique_words.size()][2];
        int i=0;
        
        // Making vectors for each unique element in the array along with their frequency in both the documents
        for(String x : unique_words)
        {
            for(String y: edited[0])
            {
                if(x.equalsIgnoreCase(y))
                    frequency[i][0]+= 1;
            }
            for(String y: edited[1])
            {
                if(x.equalsIgnoreCase(y))
                    frequency[i][1]+= 1;
            }
            
            i+=1;
        }
        
        // Normalization all the data entries in the frequency matrix
        double sum1 = 0;
        double sum2 = 0;
        for(i=0;i<unique_words.size();i++)
        {
            sum1 += frequency[i][0] * frequency[i][0];
            sum2 += frequency[i][1] * frequency[i][1];
        }
        
        sum1 = sqrt(sum1);
        sum2 = sqrt(sum2);
        
        for(i=0;i<unique_words.size();i++)
        {
            frequency[i][0] = frequency[i][0]/sum1 ;
            frequency[i][1] = frequency[i][1]/sum2 ;
        }
        
        
        // Calculating Similarities
        double sum = 0;
        for(i=0;i<unique_words.size();i++)
        {
            double x = (frequency[i][0] - frequency[i][1]) * (frequency[i][0] - frequency[i][1]);
            sum += x;
        }
        
        sum = sqrt(sum);
        
        similarity =  ((sum)/ (2*sqrt(unique_words.size())));
        System.out.println("Similarity: "+ (similarity*100) + " %");
        System.out.println();
        return similarity;
        
    }
}
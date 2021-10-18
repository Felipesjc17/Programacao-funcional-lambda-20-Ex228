package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import entities.Product;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter full file path: ");
		String path = sc.nextLine();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			
			
			// Listas de produtos
			List<Product> list = new ArrayList<>();
			
			String line = br.readLine();
			while (line != null) {
				String[] fields = line.split(",");
				list.add(new Product(fields[0], Double.parseDouble(fields[1])));
				line = br.readLine();
			}
			
			// convertendo lista para Stream
			double avg = list.stream()
					.map(p -> p.getPrice()) // Pipeline gerando novo stream com preços dos produtos 
					.reduce(0.0, (x,y) -> x + y) / list.size(); // Pipeline começando com zero, fazendo somatório dos preço e divisão pelo tamanho(size) descobrindo a média
			
			System.out.println("Average price: " + String.format("%.2f", avg)); 
			
			Comparator<String> comp = (s1, s2) -> s1.toUpperCase().compareTo(s2.toUpperCase()); //comparando Strings por ordem alfabetica ignorando letras maiúsculas e minúsculas
			
			//ordenar nomes em ordem decrescente os produtos com preço abaixo da media
			List<String> names = list.stream() // Pipeline convertendo lista para stream de nomes
					.filter(p -> p.getPrice() < avg) // pipeline filtrando preços abaixo da média
					.map(p -> p.getName()).sorted(comp.reversed()) //pipeline pegando os nomes filtrados dos preços, usando o sorted com a comparação feita acima e com o metodo reversed que compara na ordem decrescente 
					.collect(Collectors.toList()); // transformando stream em lista
			
			names.forEach(System.out::println); // imprimindo 

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		sc.close();
	}
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueadero;
import java.io.*;
import java.util.Scanner;
import java.util.Date;
/**
 *
 * @author Sala_605
 */
class Vehiculo implements Serializable {
   private String placa;
   private Date horaEntrada;
   private boolean esMoto;


   public Vehiculo(String placa, boolean esMoto) {
       this.placa = placa;
       this.horaEntrada = new Date();
       this.esMoto = esMoto;
   }


   public String getPlaca() {
       return placa;
   }


   public Date getHoraEntrada() {
       return horaEntrada;
   }


   public boolean esMoto() {
       return esMoto;
   }
}

public class Parqueadero {
    private static Vehiculo[] parqueadero;
   private static int maxParqueadero;
   //private static final String FILE_NAME = "parqueadero.dat";
   
   public static void main(String[] args) throws FileNotFoundException, IOException {
       inicializarParqueadero();
       Scanner scanner = new Scanner(System.in);


       while (true) {
           System.out.println("\n\t PARQUEADERO \n\n");
           System.out.println(" 1. CONFIGURAR ESPACIOS                              ");
           System.out.println(" 2. INGRESO DE VEHICULO                              ");
           System.out.println(" 3. SALIDA DE VEHICULO                               ");
           System.out.println(" 4. VER PARQUEADERO                                  ");
           System.out.println(" 5. VERIFICAR CUPOS LIBRES                           ");
           System.out.println(" 6. SALIR                                            ");
           System.out.println(" 7. ABRIR                                            ");
           System.out.print("\n INGRESE OPCION: ");
           


           int opc = scanner.nextInt();


           switch (opc) {
               case 1:
                   configurarEspacios(scanner);
                   break;
               case 2:
                   ingresarVehiculo(scanner);
                   break;
               case 3:
                   retirarVehiculo(scanner);
                   break;
               case 4:
                   mostrarParqueadero();
                   break;
               case 5:
                   verificarCuposLibres();
                   break;
               case 6:
                   guardarParqueadero();
                   System.exit(0);
                   break;
               case 7:
                   abrirParqueadero();
                
               default:
                   System.out.println("\n\t ** OPCION NO VALIDA ** ");
           }
       }
   }
private static void inicializarParqueadero() {
       try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Data.bin"))) {
           parqueadero = (Vehiculo[]) ois.readObject();
           maxParqueadero = parqueadero.length;
       } catch (IOException | ClassNotFoundException e) {
           maxParqueadero = 5;
           parqueadero = new Vehiculo[maxParqueadero];
       }
   }


   private static void configurarEspacios(Scanner scanner) {
       System.out.print("Ingrese la capacidad del parqueadero: ");
       maxParqueadero = scanner.nextInt();
       parqueadero = new Vehiculo[maxParqueadero];
       System.out.println("\n\t ** ESPACIOS CONFIGURADOS ** ");
   }


   private static void ingresarVehiculo(Scanner scanner) {
       if (contarVehiculos(parqueadero) >= maxParqueadero) {
           System.out.println("\n\t ** PARQUEADERO LLENO ** ");
       } else {
           System.out.print("Es un carro o una moto? (c/m): ");
           boolean esMoto = scanner.next().equalsIgnoreCase("m");


           System.out.print("Ingrese la placa del vehículo: ");
           String placa = scanner.next();


           if (!esPlacaValida(placa, esMoto)) {
               System.out.println("\n\t ** PLACA INVALIDA ** ");
               return;
           }


           Vehiculo vehiculo = new Vehiculo(placa, esMoto);
           for (int i = 0; i < maxParqueadero; i++) {
               if (parqueadero[i] == null) {
                   parqueadero[i] = vehiculo;
                   System.out.println("\n\t ** VEHICULO PARQUEADO ** ");
                   return;
               }
           }
       }
   }


   private static void retirarVehiculo(Scanner scanner) {
       if (contarVehiculos(parqueadero) == 0) {
           System.out.println("\n\t ** PARQUEADERO VACIO ** ");
       } else {
           System.out.print("Ingrese la placa del vehículo a retirar: ");
           String placa = scanner.next();
           Vehiculo vehiculo = buscarVehiculo(parqueadero, placa);


           if (vehiculo == null) {
               System.out.println("\n\t ** VEHICULO NO ENCONTRADO ** ");
               return;
           }


           long minutos = calcularTiempo(vehiculo);
           int tarifa = vehiculo.esMoto() ? 50 : 100;
           int costo = (int) minutos * tarifa;


           System.out.println("Tiempo de estadía: " + minutos + " minutos");
           System.out.println("Costo total: " + costo + " pesos");


           eliminarVehiculo(parqueadero, placa);
           System.out.println("\n\t ** VEHICULO RETIRADO ** ");
       }
   }


   private static void mostrarParqueadero() {
       if (contarVehiculos(parqueadero) == 0) {
           System.out.println("\n\t ** PARQUEADERO VACIO ** ");
       } else {
           System.out.println("\n\t ** PARQUEADERO ** ");
           for (Vehiculo vehiculo : parqueadero) {
               if (vehiculo != null) {
                   System.out.println("Placa: " + vehiculo.getPlaca() + ", Hora de entrada: " + vehiculo.getHoraEntrada());
               }
           }
       }
   }


   private static void verificarCuposLibres() {
       System.out.println("Cupos libres en parqueadero: " + (maxParqueadero - contarVehiculos(parqueadero)));
   }


   private static boolean esPlacaValida(String placa, boolean esMoto) {
       if (esMoto) {
           return placa.matches("[A-Z]{3}[0-9]{2}[A-Z]");
       } else {
           return placa.matches("[A-Z]{3}[0-9]{3}");
       }
   }


   private static Vehiculo buscarVehiculo(Vehiculo[] array, String placa) {
       for (Vehiculo vehiculo : array) {
           if (vehiculo != null && vehiculo.getPlaca().equals(placa)) {
               return vehiculo;
           }
       }
       return null;
   }


   private static void eliminarVehiculo(Vehiculo[] array, String placa) {
       for (int i = 0; i < array.length; i++) {
           if (array[i] != null && array[i].getPlaca().equals(placa)) {
               array[i] = null;
               break;
           }
       }
   }


   private static int contarVehiculos(Vehiculo[] array) {
       int count = 0;
       for (Vehiculo vehiculo : array) {
           if (vehiculo != null) {
               count++;
           }
       }
       return count;
   }


   private static long calcularTiempo(Vehiculo vehiculo) {
       Date ahora = new Date();
       return (ahora.getTime() - vehiculo.getHoraEntrada().getTime()) / (1000 * 60);
   }

   private static void guardarParqueadero() throws FileNotFoundException {
       FileOutputStream fos = new FileOutputStream("Data.bin");
       try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
           oos.writeObject(parqueadero);
           fos.close();
       } catch (IOException e) {
           System.out.println("\n\t ** ERROR AL GUARDAR ** ");
       }
   }

   private static void abrirParqueadero() throws IOException {
   
        try (InputStream inputStream = new FileInputStream("Data.bin")) {
            int data;
            while ((data = inputStream.read()) != -1) {
                System.out.println((char) data);
            }}
}  
}

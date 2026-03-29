package cinema;

import java.util.ArrayList;
import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        // Write your code here
        boolean exitProgram = false;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows: ");
        int numOfRows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row: ");
        int numOfSeatPerRow = scanner.nextInt();
        Screen screen = new Screen(numOfRows, numOfSeatPerRow);

        while (!exitProgram) {
            System.out.println("");
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1:
                    screen.printScreen();
                    break;
                case 2:
                    System.out.println("Enter a row number:");
                    int rowNumber = scanner.nextInt();
                    System.out.println("Enter a seat number in that row:");
                    int seatNumber = scanner.nextInt();
                    screen.getSeatInfo(rowNumber, seatNumber);
                    break;
                case 3:
                    screen.printStats();
                    break;
                case 0:
                    exitProgram = true;
                    break;
            }
        }
    }
}

class Screen{
    int numOfRows;
    int numOfSeatsPerRow;
    int ticketPrice = 10;
    int seatsSold = 0;
    String percentageSold = "0.00";
    int incomeSoFar = 0;
    int potentialIncome = 0;
    ArrayList<String> titleRow = new ArrayList<>();
    ArrayList<ArrayList<Seat>> rowsOfSeats = new ArrayList<>();

    public Screen(int numOfRows, int numOfSeatsPerRow){
        this.numOfRows = numOfRows;
        this.numOfSeatsPerRow = numOfSeatsPerRow;
        titleRow.add(" ");
        for (int i = 1; i <= numOfSeatsPerRow; i++){
            titleRow.add(" " + i);
        }
        for (int i = 1; i <= numOfRows; i++){
            ArrayList<Seat> row = new ArrayList<>();
            for(int z = 1; z <= numOfSeatsPerRow; z++) {
                int ticketPrice = CalcTicketPrice(i, z);
                row.add(new Seat(i, z , ticketPrice, false));
                potentialIncome += ticketPrice;
            }
            rowsOfSeats.add(row);
        }
    }
    public void printScreen(){
        System.out.println("Cinema:");
        titleRow.forEach(System.out::print);
        System.out.print("\n");
        int rowNumber = 1;
        for(ArrayList<Seat> array : rowsOfSeats){
            System.out.print(rowNumber);
            for(Seat seat : array){
                System.out.print(seat.isSeatBooked());
            }
            System.out.print("\n");
            rowNumber++;
        }
    }
    public void getSeatInfo(int rowNumber, int seatNumber){
        boolean bookingMade = false;
        while (!bookingMade) {
            try {
                Seat chosenSeat = rowsOfSeats.get(rowNumber - 1).get(seatNumber - 1);
                while (!bookingMade) {
                    if (chosenSeat.bookSeat()) {
                        System.out.println("Ticket price: $" + chosenSeat.price);
                        seatsSold++;
                        incomeSoFar += chosenSeat.getPrice();
                        updatePercentageSold();
                        bookingMade = true;
                    } else {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter a row number:");
                        rowNumber = scanner.nextInt();
                        System.out.println("Enter a seat number in that row:");
                        seatNumber = scanner.nextInt();
                        chosenSeat = rowsOfSeats.get(rowNumber - 1).get(seatNumber - 1);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("\nWrong input!\n");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter a row number:");
                rowNumber = scanner.nextInt();
                System.out.println("Enter a seat number in that row:");
                seatNumber = scanner.nextInt();
            }
        }
    }
    public int CalcTicketPrice(int rowNumber, int seatNumber){
        int numberOfCustomers = numOfRows * numOfSeatsPerRow;
        if (numberOfCustomers <= 60 ){
            return ticketPrice;
        } else {
            if (numOfRows % 2 == 0){
                int uniqueSeatNumber = rowNumber * seatNumber;
                if (uniqueSeatNumber >= (numOfRows / 2)){
                    return ticketPrice;
                } else{
                    return 8;
                }
            } else {
                int frontRows = ((numOfRows - 1) / 2);
                int backRows = numOfRows - frontRows;
                if (rowNumber <= frontRows){
                    return 10;
                } else {
                    return 8;
                }
            }
        }
    }
    public void printStats(){
        System.out.println();
        System.out.println("Number of purchased tickets: " + seatsSold);
        System.out.println("Percentage: " + percentageSold + "%");
        System.out.println("Current income: $" + incomeSoFar);
        System.out.println("Total income: $" + potentialIncome);
        System.out.println();

    }
    private void updatePercentageSold(){
        double totalSeats = numOfRows * numOfSeatsPerRow;
        double percentageValue = (seatsSold / totalSeats) * 100;
        percentageSold = String.format("%.2f", percentageValue);
    }
}

class Seat{
    int rowNumber;
    int seatNumber;
    int price;
    boolean isBooked = false;
    int uniqueSeatNumber;

    public Seat(int rowNumber, int seatNumber, int price, boolean isBooked) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.price = price;
        this.isBooked = isBooked;
        this.uniqueSeatNumber = rowNumber * seatNumber;
    }
    public String isSeatBooked(){
        if(isBooked){
            return " B";
        } else {
            return " S";
        }
    }
    public boolean bookSeat(){
        if(!isBooked) {
            this.isBooked = true;
            return true;
        } else {
            System.out.println();
            System.out.println("That ticket has already been purchased!");
            System.out.println();
            return false;
        }
    }
    public int getPrice(){
        return price;
    }
}

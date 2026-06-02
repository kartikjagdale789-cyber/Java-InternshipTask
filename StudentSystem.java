import java.util.Scanner;

class Student {
    int rollNumber;
    String name;
    double marks;

    Student(int rollNumber, String name, double marks) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.marks = marks;
    }

    void displayStudent() {
        System.out.println("Roll No: " + rollNumber +
                ", Name: " + name +
                ", Marks: " + marks);
    }
}

public class StudentSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Student[] students = new Student[100]; 
        int count = 0;
        int choice;

        do {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            switch (choice) {

                case 1:
                    if (count < students.length) {
                        System.out.print("Enter Roll Number: ");
                        int roll = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Marks: ");
                        double marks = sc.nextDouble();
                        students[count] = new Student(roll, name, marks);
                        count++;

                        System.out.println("Student added successfully.");
                    } else {
                        System.out.println("Storage full!");
                    }
                    break;

                case 2:
                    if (count == 0) {
                        System.out.println("No students available.");
                    } else {
                        for (int i = 0; i < count; i++) {
                            students[i].displayStudent();
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Roll Number to update: ");
                    int updateRoll = sc.nextInt();
                    boolean found = false;
                    for (int i = 0; i < count; i++) {
                        if (students[i].rollNumber == updateRoll) {
                            sc.nextLine();
                            System.out.print("Enter new name: ");
                            students[i].name = sc.nextLine();
                            System.out.print("Enter new marks: ");
                            students[i].marks = sc.nextDouble();
                            System.out.println("Student updated successfully.");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Roll Number to delete: ");
                    int deleteRoll = sc.nextInt();
                    boolean deleted = false;
                    for (int i = 0; i < count; i++) {
                        if (students[i].rollNumber == deleteRoll) {

                            for (int j = i; j < count - 1; j++) {
                                students[j] = students[j + 1];
                            }
                            count--;
                            deleted = true;
                            System.out.println("Student deleted successfully.");
                            break;
                        }
                    }

                    if (!deleted) {
                        System.out.println("Student not found.");
                    }
                    break;

                case 5:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }
}
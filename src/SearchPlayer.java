append("Points: ").append(rs.getDouble("points")).append("\n");
                sb.append("Rebounds: ").append(rs.getDouble("rebounds")).append("     ");
                sb.append("Assists: ").append(rs.getDouble("assists")).append("\n");
                sb.append("Steals: ").append(rs.getDouble("steals")).append("            ");
                sb.append("Blocks: ").append(rs.getDouble("blocks")).append("\n\n");
                count++; // Increment count for each player found
            }

            // Printing results or a message if no players found
            if (count == 0) {
                return "No players found with the given criteria."; // If no players found
            } else {
                return sb.toString(); // Return the accumulated result string
            }
        } catch (Exception e) {
            // Handling any exceptions that occur during database access or query execution
            e.printStackTrace();
        }
        return "Error getting players."; // Return error message if exception occurs
    }
}

<?php
function drawTicketTable(Session $session, $tickets, $type)
{
?>
<!DOCTYPE html>
<html lang="en-US">
<head>
    <title>Problem Solvers</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/ticketTable.css">
    <link rel="stylesheet" href="../styles/global.css">
</head>
<body>
    <div class="ticketTable-wrapper">
        <div class="ticket-header">
            <h2><?php echo ($type === 'client') ? 'My Tickets' : 'Open Tickets'; ?></h2>

            <form method="get" action="tickets.php">
                <select name="department">
                    <option value="">All Departments</option>
                    <option value="1">Sales</option>
                    <option value="2">Support</option>
                </select>


                <select name="status">
                    <option value="">All Status</option>
                    <option value="Open">Open</option>
                    <option value="Closed">Closed</option>
                    <!-- Add more statuses as needed -->
                </select>

                <select name="priority">
                    <option value="">All Priorities</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                </select>

                <button type="submit">Apply Filters</button>
            </form>

            <button class="add-ticket-button" id="add-button">Add Ticket</button>
            <script type="text/javascript">
                document.getElementById("add-button").onclick = function () {
                    location.href = "../pages/add_ticket_modal.php";
                };
            </script>
        </div>
        
        <table>
            <tr>    
                <th>Ticket ID</th>
                <th>Name</th>
                <th>Type</th>
                <th>Status</th>
                <th>Info</th>
                <th>Priority</th>
                <th>Agent</th>
                <th>Department</th>
                <?php if ($type !== 'client'): ?>
                    <th>Client</th>
                    <th></th>
                <?php endif; ?>
                <th></th>
            </tr>
            <?php if (empty($tickets)): ?>
                <tr>
                    <td colspan="<?php echo ($type !== 'client') ? '11' : '9'; ?>" class="empty-tickets-message">Currently you don't have any open tickets.</td>
                </tr>
            <?php else: ?>
                <?php foreach ($tickets as $ticket): ?>
                    <tr>
                        <td><?php echo $ticket['id']; ?></td>
                        <td><?php echo $ticket['name']; ?></td>
                        <td><?php echo $ticket['type']; ?></td>
                        <td><?php echo $ticket['status']; ?></td>
                        <td><?php echo $ticket['info']; ?></td>
                        <td><?php echo $ticket['priority']?: 'No Priority Defined'; ?></td>
                        <td><?php echo $ticket['agentName'] ?: 'No agent Assigned yet'; ?></td>
                        <td><?php echo $ticket['departmentName']; ?></td>
                        <?php if ($type !== 'client'): ?>
                            <td><?php echo $ticket['clientName']; ?></td>
                            <td class="actions">
                                <input type="hidden" name="ticketId" value="<?php echo $ticket['id']; ?>">
                                <a href="../pages/edit_ticket.php?ticketId=<?php echo $ticket['id']; ?>" class="edit-button">Edit</a>
                            </td>
                        <?php endif; ?>
                        <td class="actions">
                            <form method="post" action="../actions/delete_ticket.php">
                                <input type="hidden" name="ticketId" value="<?php echo $ticket['id']; ?>">
                                <button type="submit" name="deleteTicket" value="<?php echo $ticket['id']; ?>" class="delete">Delete</button>
                            </form>
                        </td>
                    </tr>
                <?php endforeach; ?>
            <?php endif; ?>
        </table>
    </div>
</body>
</html>
<?php
}
?>

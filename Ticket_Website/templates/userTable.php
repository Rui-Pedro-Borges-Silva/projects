<?php
function drawUsersTable(Session $session, $users, $type)
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
            <p>Filters</p>
            <h2><?php echo 'Active Users'; ?></h2>
        </div>
        <table>
            <tr>
                <th>User ID</th>
                <th>Name</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th></th>
                <th></th>
            </tr>
            <?php if (empty($users)): ?>
                <tr>
                    <td colspan="<?php echo ($type !== 'admin') ? '11' : '9'; ?>" class="empty-tickets-message">Currently there are no active users.</td>
                </tr>
            <?php else: ?>
                <?php foreach ($users as $user): ?>
                    <tr>
                        <td><?php echo $user['id']; ?></td>
                        <td><?php echo $user['name']; ?></td>
                        <td><?php echo $user['username']; ?></td>
                        <td><?php echo $user['email']; ?></td>
                        <td><?php echo $user['type']; ?></td>
                        <?php if ($type === 'admin'): ?>
                            <td class="actions">
                                <input type="hidden" name="userId" value="<?php echo $user['id']; ?>">
                                <a href="../pages/edit_user.php?userId=<?php echo $user['id']; ?>" class="edit-button">Edit</a>
                            </td>
                        <?php endif; ?>
                        <td class="actions">
                        <form method="post" action="../actions/action_deleteUser.php">
                                <input type="hidden" name="userId" value="<?php echo $user['id']; ?>">
                                <button type="submit" name="deleteUser" value="<?php echo $user['id']; ?>" class="delete">Delete</button>
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

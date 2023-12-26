<?php 
    declare(strict_types = 1);
    
    function drawAddTicketModal($session, $type, $agents, $departments)
    {
        ?>
        <!-- Add Ticket Component -->
        <div class="add-ticket-component">
            <h3>Add Ticket</h3>
            <form class="add-ticket-form" action="../actions/action_addticket.php" method="post">
                <!-- Add ticket form inputs -->
                <input type="text" name="ticketName" placeholder="Ticket Name" required>
                <select name="ticketType">
                    <option value="" disabled selected>Ticket Type</option>
                    <?php foreach($departments as $department):?>
                        <option value="<?php echo $department->name; ?>"><?php echo $department->name; ?></option>
                    <?php endforeach;?>
                </select>
                <input type="text" name="ticketInfo" placeholder="Ticket Info" required>
                <?php if($type === 'admin'):?>
                    <select name="priority">
                        <option value="" disabled selected>Ticket Priority</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                    </select>
                    <select name="agent">
                        <option value="" disabled selected>Assigned Agent</option>
                        <?php foreach($agents as $agent):?>
                            <option value="<?php echo $agent->name; ?>"><?php echo $agent->name; ?></option>
                        <?php endforeach; ?>
                    </select>            
                <?php endif;?>
                <button class="add-ticket-button" type="submit">Add Ticket</button>
            </form>
        </div>
        <link rel="stylesheet" href="../styles/add_ticket_modal.css">
        <?php
    }
    ?>

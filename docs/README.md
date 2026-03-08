# Perlica User Guide

**Perlica** is a personal task management assistant optimized for use via a Command Line Interface (CLI). Perlica is capable of tracking three different types of tasks (ToDos, Deadlines, and Events) and provides various administrative commands to organize your daily schedule.

## Features

### Adding Tasks

You can add three types of tasks: ToDos, Deadlines, and Events.

#### `todo`
Adds a basic task without any date or time attached to it.
* **Format:** `todo <description>`
* **Example:** `todo read book`

#### `deadline`
Adds a task that needs to be done before a specific date/time. The app supports parsing common date and time formats.
* **Format:** `deadline <description> /by <date/time>`
* **Accepted Date Formats:** `yyyy-MM-dd HHmm`, `d/M/yyyy HHmm`, `yyyy-MM-dd`, `d/M/yyyy`
* **Example:** `deadline return book /by 2/12/2019 1800`

#### `event`
Adds a task that starts at a specific date/time and ends at a specific date/time.
* **Format:** `event <description> /from <start date/time> /to <end date/time>`
* **Example:** `event project meeting /from Mon 2pm /to 4pm`

### Managing Tasks

#### `list`
Shows a list of all current tasks.
* **Format:** `list`

#### `mark`
Marks a task as done based on its index in the task list.
* **Format:** `mark <list_index>`
* **Example:** `mark 2` (marks the 2nd task from the list as done)

#### `unmark`
Marks a task as not done based on its index in the task list.
* **Format:** `unmark <list_index>`
* **Example:** `unmark 2` (marks the 2nd task from the list as not done)

#### `delete`
Deletes an existing task from your list based on its index. Alternatively, deletes all tasks.
* **Format:** `delete <list_index>` or `delete all`
* **Example:** `delete 3` (deletes the 3rd task from the list)
* **Example:** `delete all` (clears the entire task list)

### Finding Tasks

#### `find`
Finds tasks whose descriptions contain the given keyword.
* **Format:** `find <keyword>`
* **Example:** `find book` (returns all tasks containing the word "book")

### Exiting the Application

#### `bye`
Exits the application gracefully. All tasks are automatically saved to your hard disk on exit.
* **Format:** `bye`

## Storage
Perlica data is automatically saved to the hard disk at `./data/perlica-list.txt` after any command that changes the data. There is no need to manually save.

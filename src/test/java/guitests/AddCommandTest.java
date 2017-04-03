package guitests;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import guitests.guihandles.TodoCardHandle;
import seedu.todolist.commons.core.Messages;
import seedu.todolist.commons.exceptions.IllegalValueException;
import seedu.todolist.logic.commands.AddCommand;
import seedu.todolist.testutil.TestTodo;
import seedu.todolist.testutil.TestUtil;
import seedu.todolist.testutil.TodoBuilder;

public class AddCommandTest extends TodoListGuiTest {

    @Test
    public void add() {
        //add one todo
        TestTodo[] currentList = td.getTypicalTodos();
        TestTodo todoToAdd = td.laundry;

        assertAddSuccess(todoToAdd, currentList);
        currentList = TestUtil.addTodosToList(currentList, todoToAdd);

        //add another todo
        todoToAdd = td.shopping;
        assertAddSuccess(todoToAdd, currentList);
        currentList = TestUtil.addTodosToList(currentList, todoToAdd);

        //add duplicate todo
        commandBox.runCommand(td.laundry.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TODO);
        assertTrue(todoListPanel.isListMatching(true, currentList));

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.dog);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

    }

    @Test
    public void addEventTest() {
        TestTodo[] currentList = td.getTypicalTodos();
        TestTodo todoToAdd = td.lunch;
        assertAddSuccess(todoToAdd, currentList);
    }

    @Test
    public void addDeadLineTest() {
        TestTodo[] currentList = td.getTypicalTodos();
        TestTodo todoToAdd = td.job;
        assertAddSuccess(todoToAdd, currentList);
    }
    @Test
    public void addEventByDateTest() {
        TestTodo[] currentList = td.getTypicalTodos();
        TestTodo todoToAdd;
        try {
            todoToAdd = new TodoBuilder().withName("DefaultEvent").
                    withStartTime("12:00AM 11/11/11").
                    withEndTime("12:00AM 11/11/11").build();

            commandBox.runCommand("add DefaultEvent s/11/11/11 e/11/11/11");

            TodoCardHandle addedCard = todoListPanel.navigateToTodo(todoToAdd.getName().fullName);
            assertMatching(todoToAdd, addedCard);

            TestTodo[] expectedList = TestUtil.addTodosToList(currentList, todoToAdd);
            assertTrue(todoListPanel.isListMatching(true, expectedList));
        } catch (IllegalValueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void getTimeByDefaultTest() {
        try {
            Set<String> str = new HashSet<String>();
            AddCommand addComand = new AddCommand("testGetTime", str);

            TestTodo[] currentList = td.getTypicalTodos();

            TestTodo eventToAdd = new TodoBuilder().withName("DefaultEvent").
                    withStartTime(getTodayMidnighttoString(addComand.getTodayMidnight())).
                    withEndTime(getTomorrowMidnighttoString(addComand.getTomorrowMidnight())).build();

            assertAddSuccess(eventToAdd, currentList);

            commandBox.runCommand("undo");

            TestTodo deadLineToAdd = new TodoBuilder().withName("DefaultDeadLine").
                    withEndTime(getTomorrowMidnighttoString(addComand.getTomorrowMidnight())).build();

            assertAddSuccess(deadLineToAdd, currentList);

        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : " get time fail";
        }
    }

    @Test
    public void recurringAddTest() {
        TestTodo[] currentList = td.getTypicalTodos();
        String name = "recurringAdd";
        String startTime = "11:00AM";
        String endTime = "12:00PM";
        String tags = "recurringtags";
        String startDate = "6/4/17";
        String endMonth = "5/17";
        TestTodo[] addTestTodoList = new TestTodo[8];
        String[] addDate = { "6/4/17", "13/4/17", "20/4/17", "27/4/17", "4/5/17",
            "11/5/17", "18/5/17", "25/5/17" };
        String[] addDateName = { "06 04 17", "13 04 17", "20 04 17", "27 04 17", "04 05 17",
            "11 05 17", "18 05 17", "25 05 17" };
        for (int i = 0; i < 8; i++) {
            try {
                addTestTodoList[i] = new TodoBuilder().withName(name + addDateName[i]).
                        withStartTime(startTime + " " + addDate[i]).
                        withEndTime(endTime + " " + addDate[i]).withTags(tags).build();
            } catch (IllegalValueException e) {
                e.printStackTrace();
            }
        }
        assertRecurringddSuccess(name + " " + startTime + " " + endTime +
                " " + tags + " " + startDate + " " + endMonth, addTestTodoList, currentList);
    }

//add taskname s/11:00AM e/12:00PM t/recurringEvent r/6/4/2017 4/2017

    @Test
    public void invalidDateTimeInputTest() {
        commandBox.runCommand("add invalidDateTimeInput s/11");
        assertResultMessage(AddCommand.MESSAGE_INVALID_TIME);

        commandBox.runCommand("adds Johonny s/");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(TestTodo todoToAdd, TestTodo... currentList) {

        commandBox.runCommand(todoToAdd.getAddCommand());

        //confirm the new card contains the right data
        TodoCardHandle addedCard = todoListPanel.navigateToTodo(todoToAdd.getName().fullName);
        assertMatching(todoToAdd, addedCard);

        //confirm the list now contains all previous todos plus the new todo
        TestTodo[] expectedList = TestUtil.addTodosToList(currentList, todoToAdd);
        assertTrue(todoListPanel.isListMatching(true, expectedList));
    }

    private void assertRecurringddSuccess(String commandInfo, TestTodo[] addTestTodoList, TestTodo... currentList) {

        String[] addCommand = commandInfo.split(" ");
        commandBox.runCommand("add" + " " + addCommand[0] + " " + "s/" + addCommand[1] + " " + "e/" +
                addCommand[2] + " " + "t/" + addCommand[3] + " " + "r/" + addCommand[4] + " " + addCommand[5]);
        for (int i = 0; i < addTestTodoList.length; i++) {
          //confirm the new card contains the right data
            TodoCardHandle addedCard = todoListPanel.navigateToTodo(addTestTodoList[i].getName().fullName);
            assertMatching(addTestTodoList[i], addedCard);

        }
        /*
        //confirm the list now contains all previous todos plus the new todo
        TestTodo[] expectedList = TestUtil.addTodosToList(currentList, todoToAdd);
        assertTrue(todoListPanel.isListMatching(true, expectedList));
        */
    }

    private String getTomorrowMidnighttoString(Date dt) {
        Date dtAssign = dt;
        Calendar c = Calendar.getInstance();
        c.setTime(dtAssign);
        c.add(Calendar.DATE, 1);
        dtAssign = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "12:00AM" + " " + dateFormat.format(dtAssign);
    }

    private String getTodayMidnighttoString(Date dt) {
        Date dtAssign = dt;
        Calendar c = Calendar.getInstance();
        c.setTime(dtAssign);
        dtAssign = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "12:00AM" + " " + dateFormat.format(dtAssign);
    }
}

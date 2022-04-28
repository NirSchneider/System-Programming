package bgu.spl.net.impl.BGRS;

import bgu.spl.net.api.MessagingProtocol;

public class BGRSProtocol implements MessagingProtocol<Message> {

    private Database database = Database.getInstance();
    private User user;
    private boolean alreadyConnected = false;
    private boolean shouldTerminate = false;


    @Override
    public Message process(Message msg) {
        CommandClientToServer command = (CommandClientToServer) msg;
        switch(command.getOpcode())
        {

            case 1: case 2:
            {
                 return registeration(command);
            }
            case 3:
            {
                return login(command);
            }
            case 4:
            {
                return logout();
            }
            case 5:
            {
                return courseReg(command.getCourseNumber());
            }
            case 6:
            {
                return kdamCheck(command.getCourseNumber());
            }
            case 7:
            {
                return courseStat(command.getCourseNumber());
            }
            case 8:
            {
                return studentStat(command.getUserName());
            }
            case 9:
            {
                return isRegister(command.getCourseNumber());
            }
            case 10:
            {
                return unregister(command.getCourseNumber());
            }
            case 11:
            {
                return myCourses();
            }
        }

        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }

    public Message registeration(CommandClientToServer command)
    {
        synchronized (database.getUsersConnected())
        {
            User user = database.getUsersConnected().get(command.getUserName());
            if(this.alreadyConnected || (user != null && userExist(user)))
            {
                ErrorCommand err;
                if(user instanceof Admin)
                    err = new ErrorCommand((short)1);
                else
                    err = new ErrorCommand((short)2);
                return err;
            }
            else
            {
                if(command.getOpcode()==1)
                    user = new Admin(command.getUserName(),command.getPassword());
                else
                    user = new Student(command.getUserName(),command.getPassword());
                database.getUsersConnected().putIfAbsent(user.getUserName(), user);
                AckCommand ack;
                if(user instanceof Admin)
                    ack = new AckCommand((short)1);
                else
                    ack = new AckCommand((short)2);
                return ack;
            }
        }

    }

    public Message login(CommandClientToServer command)
    {
        if(!this.alreadyConnected)
        {
            User user = database.getUsersConnected().get(command.getUserName());
            this.user = user;
        }
        if(this.alreadyConnected || user == null || !userExist(user) || !database.getUsersConnected().get(user.getUserName()).getPassword().equals(command.getPassword()) || user.getIsConnected())
        {
            ErrorCommand err = new ErrorCommand((short)3);
            return err;
        }
        else
        {
            this.alreadyConnected = true;
            user.setConnedcted(true);
            AckCommand ack = new AckCommand((short)3);
            return ack;
        }
    }

    public Message logout()
    {
        if(!this.alreadyConnected || !userExist(user) || !user.getIsConnected())
        {
            ErrorCommand err = new ErrorCommand((short)4);
            return err;
        }
        else
        {
            this.alreadyConnected = false;
            user.setConnedcted(false);
            AckCommand ack = new AckCommand((short)4);
            this.shouldTerminate = true;
            return ack;
        }
    }

    public Message courseReg(short courseNum)
    {
        if(!regConditionsOK(courseNum))
        {
            ErrorCommand err = new ErrorCommand((short)5);
            return err;
        }
        else
        {
            ((Student)user).registerCourse(courseNum); // if got to here must be a student type
            AckCommand ack = new AckCommand((short)5);
            return ack;
        }
    }

    public Message kdamCheck(short courseNum)
    {
        if(!this.alreadyConnected || !courseExist(courseNum) || !(user instanceof Student))
        {
            ErrorCommand err = new ErrorCommand((short)6);
            return err;
        }
        else
        {
            String kdamList = database.getCourses().get(courseNum).getKdamCourses().toString().replace(" ","");
            AckCommand ack = new AckCommand((short)6,kdamList);
            return ack;
        }
    }

    public Message courseStat(short courseNum)
    {
        if (!this.alreadyConnected || !(user instanceof Admin) || !courseExist(courseNum))
        {
            ErrorCommand err = new ErrorCommand((short)7);
            return err;
        }
        else
        {
            String course = database.getCourses().get(courseNum).toString();
            AckCommand ack = new AckCommand((short)7,course);
            return ack;
        }
    }

    public Message studentStat(String userName)
    {
        User requested = database.getUsersConnected().get(userName);
        if(!this.alreadyConnected || !(user instanceof Admin) || !userExist(user) || requested == null || requested instanceof Admin)
        {
            ErrorCommand err = new ErrorCommand((short)8);
            return err;
        }
        else
        {
            String student = requested.toString();
            AckCommand ack = new AckCommand((short)8,student);
            return ack;
        }

    }

    public Message isRegister(short courseNum)
    {
        if(!this.alreadyConnected || !(user instanceof Student) || !userExist(user) || !courseExist(courseNum))
        {
            ErrorCommand err = new ErrorCommand((short)9);
            return err;
        }
        else
        {
            String s = "";
            if(((Student)user).isRegToCourse(courseNum))
                s += "REGISTERED";
            else
                s += "NOT REGISTERED";

            AckCommand ack = new AckCommand((short)9,s);
            return ack;
        }
    }

    public Message unregister(short courseNum)
    {
        if(!this.alreadyConnected || !(user instanceof Student) || !courseExist(courseNum) || !userExist(user) || !((Student)user).isRegToCourse(courseNum))
        {
            ErrorCommand err = new ErrorCommand((short)10);
            return err;
        }
        Student student = (Student)user;
        student.unregisterCourse(courseNum);

        AckCommand ack = new AckCommand((short)10);
        return ack;
    }

    public Message myCourses()
    {
        if(!this.alreadyConnected || !(user instanceof Student) || !userExist(user))
        {
            ErrorCommand err = new ErrorCommand((short)11);
            return err;
        }

        String coursesList = ((Student)user).getListOfCourses().toString().replace(" ",""); //need square brackets

        AckCommand ack = new AckCommand((short)11,coursesList);
        return ack;
    }

    //Queries
    public boolean userExist(User user){return database.getUsersConnected().get(user.getUserName()) != null;}
    public boolean courseExist(short courseNum){return database.getCourses().get(courseNum) != null;}
    public boolean regConditionsOK(short courseNum)
    {
        if(!this.alreadyConnected)
        {
            return false;
        }
        else if(user instanceof Admin)
        {
            return false;
        }
        else if(!userExist(user))
        {
            return false;
        }
        else if(!courseExist(courseNum))
        {
            return false;
        }
        else if(database.getCourses().get(courseNum).isFull())
        {
            return false;
        }

        Student student = (Student)user;//if got to here must be student type

        if(!student.hasKdam(courseNum))
        {
            return false;
        }
        else if(!student.getIsConnected())
        {
            return false;
        }
        else if(student.isRegToCourse(courseNum))
        {
            return false;
        }
        return true;
    }

}

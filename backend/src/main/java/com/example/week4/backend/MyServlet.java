/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.week4.backend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

    private ArrayList<Persoon> personen = new ArrayList<Persoon>();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String sNaam = req.getParameter("naam");
        if (sNaam.toLowerCase().equals("clear")){
            personen.clear();
            return;
        }

        Integer iLeeftijd = Integer.parseInt(req.getParameter("leeftijd"));

        if(sNaam == null) {
            resp.getWriter().println("Vul een naam in");
            return;
        }if(iLeeftijd == 0 || iLeeftijd == null){
            resp.getWriter().println("Vul de leeftijd in");
            return;
        }

        addPersoon(sNaam, iLeeftijd);

        Persoon oudstePersoon = getOudsePersoon();

        resp.getWriter().println("De oudste persoon is " + oudstePersoon.Leeftijd + " jaar en zijn/haar naam is: " + oudstePersoon.Naam);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
//        String sNaam = req.getParameter("naam");
//        Integer iLeeftijd =  Integer.parseInt(req.getParameter("leeftijd"));
//
//        resp.setContentType("text/plain");
//        if(sNaam == null) {
//            resp.getWriter().println("Vul een naam in");
//        }if(iLeeftijd == 0 || iLeeftijd == null){
//            resp.getWriter().println("Vul de leeftijd in");
//        }
//
//        Persoon oudstePersoon = getOudsePersoon();
//
//        resp.getWriter().println("De oudste persoon is " + oudstePersoon.Leeftijd + " jaar en zijn/haar naam is: " + oudstePersoon.Naam);
    }
    private void addPersoon(String sNaam, Integer iLeeftijd){
        Persoon p = new Persoon();
        p.Leeftijd = iLeeftijd;
        p.Naam = sNaam;

        personen.add(p);
    }
    private Persoon getOudsePersoon(){
        Persoon resultPersoon = null;
        Persoon MyPersoon = null;

        if (personen.size() == 0){
            Persoon TestPersoon = new Persoon();
            TestPersoon.Naam = "Test";
            TestPersoon.Leeftijd = 20;
                        personen.add(TestPersoon);
        }

        for (int i = 0; i < personen.size(); i++){
            MyPersoon = personen.get(i);

            if(resultPersoon == null){
                resultPersoon = MyPersoon;
            }

            if(MyPersoon.Leeftijd > resultPersoon.Leeftijd){
                resultPersoon = MyPersoon;
            }
        }

        return resultPersoon;
    }

    class Persoon{
        public String Naam;
        public Integer Leeftijd;
    }
}

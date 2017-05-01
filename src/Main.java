import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class Main {
	
	public static void main(String []args) throws TwitterException, IOException {
		ConfigurationBuilder cb = new 	ConfigurationBuilder();
		//Twitter twitter = TwitterFactory.getSingleton();
		
		File file = new File("out.txt");
		FileWriter fw = new FileWriter(file);

		long time_seconds = 60; //seconds
		Interval interval_ob = new Interval(time_seconds * 30);
		
		//CHANGE
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("ltis54oHBgyh4eR8SFNLyCN04")
		  .setOAuthConsumerSecret("wUDMjf9duV37HsGEe1o4zJDt16x3xoqknZriRHAWUKPUMU5aTA")
		  .setOAuthAccessToken("3290557754-7zkrVwULqFhwVBWLkwzIQPTMGP256EzRwZOEoeA")
		  .setOAuthAccessTokenSecret("OY1rv66DTxhjyFECXplrRHAGOjelRx29co0jk4liMuSPK");
		
		String twitter_name = "HookyTheGame";
		
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		

		while(true) {
			Get_my_followers(twitter_name ,twitter , fw);
			Follow_Followers(twitter ,twitter_name);
			Follow_Retweats(twitter , twitter_name);
			interval_ob.sleep();
		}
		
		
	}
	static void Follow_Followers(Twitter twitter, String twitter_name) throws TwitterException {
		  long cursor = -1;
	      IDs ids;
	      User user;
	      Relationship relationship = null;
	      System.out.println("Listing followers's ids.");
	      do {
	              ids = twitter.getFollowersIDs(twitter_name, cursor);
	          for (long id : ids.getIDs()) {
	        	  user = twitter.showUser(id);
	              relationship = twitter.showFriendship(twitter_name,user.getScreenName());
	              
	              if(relationship.isTargetFollowedBySource() == true) {
	            	  System.out.println("BREAKING");
	            	  break;
	              }
	              twitter.createFriendship(id);
	        	  System.out.println(user.getScreenName());
	              

	          }
	      } while ((cursor = ids.getNextCursor()) != 0 && relationship.isTargetFollowedBySource() == false);
	}
	
	
	static void Follow_Retweats(Twitter twitter , String twitter_name) throws TwitterException{
	    List<Status> statuses = twitter.getHomeTimeline();
	    
	    //System.out.println("Showing home timeline.");
	    for (Status status : statuses) {
	        System.out.println(status.getUser().getName() + ":" + status.getText());
	        	if(status.isFavorited() == false && status.isRetweet() == true) {
	        		twitter.createFavorite(status.getId());
	        	}
	        	if(status.isRetweeted() == false &&  status.isRetweet() == false ) {
	        		twitter.retweetStatus(status.getId());
	        	}
	        if(status.isRetweet() &&  status != null) {
	        	Relationship relationship = twitter.showFriendship(twitter_name, status.getRetweetedStatus().getUser().getScreenName());
	        	if(relationship.isTargetFollowedBySource() == false) {
	        		twitter.createFriendship( status.getRetweetedStatus().getUser().getId());
	        	}
	        }
	    }
	}
	
	static void Get_my_followers(String twitter_name , Twitter twitter, FileWriter fw) {
		User user;
		try {
			user = twitter.showUser(twitter_name);
			System.out.println("---------------------------------------------------");
			LocalDateTime timePoint = LocalDateTime.now();
			System.out.println("YOU HAVE " + user.getFollowersCount()+ " FOLLOWERS" + "| TIME :"+ timePoint);
			System.out.println("---------------------------------------------------");
			
			fw.write("---------------------------------------------------"+System.lineSeparator());
			fw.write("YOU HAVE " + user.getFollowersCount()+ " FOLLOWERS" + "| TIME :"+ timePoint + System.lineSeparator());
			fw.write("---------------------------------------------------"+System.lineSeparator());
			fw.flush();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}






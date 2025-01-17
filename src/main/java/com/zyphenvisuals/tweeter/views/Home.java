package com.zyphenvisuals.tweeter.views;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyphenvisuals.tweeter.components.Tweet;
import com.zyphenvisuals.tweeter.model.PostTweetRequest;
import com.zyphenvisuals.tweeter.model.TweetModel;
import com.zyphenvisuals.tweeter.model.UrlParam;
import com.zyphenvisuals.tweeter.network.NetworkController;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

import java.lang.reflect.Type;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    public VBox tweetContainer;

    @FXML
    public HBox composeContainer;

    @FXML
    public TextArea tweetInput;

    @FXML
    public Button sendTweetButton;

    @FXML
    public Text characterCount;

    private final Gson gson = new Gson();
    private final Validator validator = new Validator();

    private int oldestTweet;
    private Boolean moreToLoad = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // load tweets
        HttpResponse<String> tweetsResponse = NetworkController.sendGetRequest("/tweet/list", List.of());

        if(tweetsResponse.statusCode() == 200) {
            // deserialize
            Type listType = new TypeToken<List<TweetModel>>(){}.getType();
            List<TweetModel> tweetModels = gson.fromJson(tweetsResponse.body(), listType);

            // add objects
            addTweets(tweetModels);
        } else {
            Text error = new Text("Error loading tweets");
            error.getStyleClass().add("title-1");
            error.getStyleClass().add("text");
            error.getStyleClass().add("danger");
            tweetContainer.getChildren().add(error);
            Text error2 = new Text("Please try to log in again.");
            error2.getStyleClass().add("title-4");
            tweetContainer.getChildren().add(error2);
        }

        // initialize validators
        validator.createCheck()
                .dependsOn("text", tweetInput.textProperty())
                .withMethod(c -> {
                    String tweetContent = c.get("text");

                    // side effect, update counter
                    characterCount.setText(tweetContent.trim().length() + "/300");

                    if (tweetContent.trim().isEmpty()) {
                        c.error("Can't post an empty tweet.");
                    }

                    if(tweetContent.trim().length() > 300) {
                        c.error("Maximum tweet length is 300 characters.");
                    }
                })
                .immediate();

        // wrap the send button
        Node parentOfButton = sendTweetButton.getParent();
        TooltipWrapper<Button> sendTweetWrapper = new TooltipWrapper<>(
                sendTweetButton,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot send tweet:\n", validator.createStringBinding())
        );
        ((HBox)parentOfButton).getChildren().add(sendTweetWrapper);
    }

    private void addTweets(List<TweetModel> tweets){
        // steal the button
        Node button = tweetContainer.getChildren().getLast();
        tweetContainer.getChildren().removeLast();

        // add the tweets
        for (TweetModel tweetModel : tweets) {
            tweetContainer.getChildren().add(new Tweet(tweetModel));
            oldestTweet = tweetModel.getId();
        }

        // decide if there are more tweets to load
        moreToLoad = tweets.size() == 10;

        // add the button back if necessary
        if(moreToLoad) {
            tweetContainer.getChildren().add(button);
        }
    }

    public void openCompose(ActionEvent actionEvent) {
        System.out.println("Opening compose");
        TranslateTransition moveUp = new TranslateTransition();
        moveUp.setNode(composeContainer);
        moveUp.setDuration(Duration.millis(300));
        moveUp.setToY(-400);
        moveUp.setAutoReverse(false);
        moveUp.play();
    }

    public void closeCompose(ActionEvent actionEvent) {
        System.out.println("Close compose");
        TranslateTransition moveUp = new TranslateTransition();
        moveUp.setNode(composeContainer);
        moveUp.setDuration(Duration.millis(300));
        moveUp.setInterpolator(Interpolator.EASE_BOTH);
        moveUp.setToY(0);
        moveUp.setAutoReverse(false);
        moveUp.play();
    }

    public void sendTweet(ActionEvent actionEvent) {
        // temporarily disable the button
        sendTweetButton.getParent().setDisable(true);

        // log
        System.out.println("Sending tweet");

        String tweetContent = tweetInput.getText().trim();

        HttpResponse<String> response = NetworkController.sendPostRequest("/tweet/post", new PostTweetRequest(tweetContent));

        if(response.statusCode() == 200) {
            System.out.println("Tweet sent");
            tweetInput.clear();
            closeCompose(actionEvent);
            sendTweetButton.getParent().setDisable(false);

            // add the tweet to the screen
            TweetModel tweetModel = gson.fromJson(response.body(), TweetModel.class);
            tweetContainer.getChildren().addFirst(new Tweet(tweetModel));
        } else {
            System.out.println("Sending tweet failed with code:" + response.statusCode());
            sendTweetButton.getParent().setDisable(false);
        }
    }

    public void loadMoreTweets(ActionEvent actionEvent) {
        // load tweets
        HttpResponse<String> tweetsResponse = NetworkController.sendGetRequest("/tweet/list", List.of(new UrlParam("before", String.valueOf(oldestTweet))));

        if(tweetsResponse.statusCode() == 200) {
            // deserialize
            Type listType = new TypeToken<List<TweetModel>>(){}.getType();
            List<TweetModel> tweetModels = gson.fromJson(tweetsResponse.body(), listType);

            // add objects
            addTweets(tweetModels);
        }
    }
}

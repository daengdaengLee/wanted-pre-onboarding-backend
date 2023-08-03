package io.github.daengdaenglee.wantedpreonboardingbackend.post.domain;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import jakarta.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    protected Post() {
    }

    public Post(String title, String content, Long authorId) {
        this.title = title;
        this.content = content;
        this.author = new User(authorId);
    }

    public Long id() {
        return this.id;
    }

    public String title() {
        return this.title;
    }

    public String content() {
        return this.content;
    }

    public User author() {
        return this.author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

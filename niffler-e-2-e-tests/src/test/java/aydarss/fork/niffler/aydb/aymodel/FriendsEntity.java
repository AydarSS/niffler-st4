package aydarss.fork.niffler.aydb.aymodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@Entity
@Table(name = "friendship")
@IdClass(FriendsId.class)
public class FriendsEntity {

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @Id
  @ManyToOne
  @JoinColumn(name = "friend_id", referencedColumnName = "id")
  private UserEntity friend;

  @Column(name = "pending")
  private boolean pending;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    FriendsEntity that = (FriendsEntity) o;
    return user != null && Objects.equals(user, that.user)
        && friend != null && Objects.equals(friend, that.friend);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(user, friend);
  }
}

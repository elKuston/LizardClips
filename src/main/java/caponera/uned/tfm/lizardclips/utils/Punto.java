package caponera.uned.tfm.lizardclips.utils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Punto {
    private int x, y;

    public Punto(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point getPoint() {
        return new Point(x, y);
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

}

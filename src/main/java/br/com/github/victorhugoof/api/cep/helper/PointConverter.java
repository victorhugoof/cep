package br.com.github.victorhugoof.api.cep.helper;

import static java.util.Objects.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PointConverter {
    private final BigDecimal longitude;
    private final BigDecimal latitude;

    public PointConverter(Point point) {
        if (nonNull(point)) {
            this.longitude = BigDecimal.valueOf(point.getX());
            this.latitude = BigDecimal.valueOf(point.getY());
        } else {
            this.longitude = null;
            this.latitude = null;
        }
    }

    public Point getPoint() {
        if (nonNull(this.longitude) && nonNull(this.latitude)) {
            return new Point(this.longitude.doubleValue(), this.latitude.doubleValue());
        }
        return null;
    }
}

package api.boardAPI.global.wrap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Wrap<T> {

    private T wrap;
}

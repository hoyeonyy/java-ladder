package ladder.controller;

import java.util.List;
import java.util.stream.IntStream;
import ladder.domain.Height;
import ladder.domain.Ladder;
import ladder.domain.PlayerName;
import ladder.domain.linegenerator.LinePatternGenerator;
import ladder.domain.linegenerator.RandomBinarySupplier;
import ladder.dto.LadderDto;
import ladder.dto.LineDto;
import ladder.dto.PlayerNamesDto;
import ladder.view.InputView;
import ladder.view.OutputView;

public class LadderGameController {
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        List<String> names = inputView.inputPlayerNames();
        List<PlayerName> playerNames = names.stream()
                .map(PlayerName::new)
                .toList();

        Height height = new Height(inputView.inputHeight());
        LinePatternGenerator lineGenerator = new LinePatternGenerator(new RandomBinarySupplier());
        Ladder ladder = new Ladder(height, playerNames.size(), lineGenerator);

        PlayerNamesDto playerNamesDto = toPlayerDto(playerNames);
        LadderDto ladderDto = toLadderDto(ladder);
        outputView.printResult(ladderDto, playerNamesDto);
    }

    private static PlayerNamesDto toPlayerDto(List<PlayerName> playerNames) {
        List<String> resultPlayerNames = playerNames.stream()
                .map(PlayerName::getName)
                .toList();
        PlayerNamesDto playerNamesDto = new PlayerNamesDto(resultPlayerNames);
        return playerNamesDto;
    }

    private LadderDto toLadderDto(Ladder ladder) {
        List<LineDto> lineDtos = IntStream.range(0, ladder.getHeight())
                .mapToObj(height -> toLineDto(ladder, height))
                .toList();
        return new LadderDto(lineDtos);
    }


    private LineDto toLineDto(Ladder ladder, int height) {
        List<Boolean> sticks = IntStream.range(0, ladder.getWidth())
                .mapToObj(width -> ladder.isExist(height, width))
                .toList();
        return new LineDto(sticks);
    }

}